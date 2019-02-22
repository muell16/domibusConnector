package eu.domibus.webadmin.jsf;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.event.AjaxBehaviorEvent;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;

import eu.domibus.webadmin.dao.IDomibusWebAdminReportDao;
import eu.domibus.webadmin.model.connector.PeriodDO;
import eu.domibus.webadmin.model.connector.PeriodEntryDO;
import org.springframework.beans.factory.annotation.Autowired;

@Controller("reportsBean")
@Scope("session")
public class ReportsBean implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 3507794352791840241L;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

    private Date fromDate;
    private Date toDate;
    private boolean includeEvidences;
    private boolean reportGenerated;
    private List<PeriodEntryDO> result;
    private Map<String, PeriodDO> periodMap;
    private List<PeriodDO> periods;

    @Autowired
    private IDomibusWebAdminReportDao reportDao;

    public String generateReport() {
        if (fromDate == null) {
            try {
                fromDate = sdf.parse("01.01.2000");
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        if (toDate == null) {
            toDate = new Date();
        }

        if (includeEvidences) {
            setResult(reportDao.loadReportWithEvidences(fromDate, toDate));
        } else {
            setResult(reportDao.loadReport(fromDate, toDate));
        }

        periodMap = new HashMap<String, PeriodDO>();

        for (PeriodEntryDO entry : result) {
            String period = entry.getMonth() + "/" + entry.getYear();
            if (!periodMap.containsKey(period)) {
                PeriodDO p = new PeriodDO();
                p.setPeriod(period);
                p.setYear(entry.getYear());
                p.setMonth(entry.getMonth().length() > 1 ? entry.getMonth() : "0" + entry.getMonth());
                p.setEntries(new ArrayList<PeriodEntryDO>());
                periodMap.put(period, p);
            }

            periodMap.get(period).getEntries().add(entry);
            periodMap.get(period).setSumReceived(periodMap.get(period).getSumReceived() + entry.getReceived());
            periodMap.get(period).setSumSent(periodMap.get(period).getSumSent() + entry.getSent());
        }

        periods = new ArrayList<PeriodDO>(periodMap.size());
        periods.addAll(periodMap.values());

        Collections.sort(periods, new Comparator<PeriodDO>() {
            @Override
            public int compare(PeriodDO c1, PeriodDO c2) {

                String p1 = c1.getYear() + c1.getMonth();
                String p2 = c2.getYear() + c2.getMonth();

                return p1.compareTo(p2);
            }
        });

        this.reportGenerated = true;

        return "/pages/reports.xhtml";
    }

    public void postProcessExcel(Object document) {
        HSSFWorkbook wb = (HSSFWorkbook) document;
        Map<String, CellStyle> styles = createStyles(wb);
        wb.setSheetName(0, sdf.format(fromDate) + " - " + sdf.format(toDate));
        HSSFSheet sheet = wb.getSheetAt(0);

        //turn off gridlines
        sheet.setDisplayGridlines(false);
        sheet.setPrintGridlines(false);
        sheet.setFitToPage(true);
        sheet.setHorizontallyCenter(true);
        PrintSetup printSetup = sheet.getPrintSetup();

        //the following three statements are required only for HSSF
        sheet.setAutobreaks(true);
        printSetup.setFitHeight((short) 1);
        printSetup.setFitWidth((short) 1);

        HSSFRow headerRow = sheet.getRow(0);
        HSSFCell cell0 = headerRow.createCell(0);
        cell0.setCellValue("Party");
        cell0.setCellStyle(styles.get("header"));
        HSSFCell cell1 = headerRow.createCell(1);
        cell1.setCellValue("Service");
        cell1.setCellStyle(styles.get("header"));
        HSSFCell cell2 = headerRow.createCell(2);
        cell2.setCellValue("Messages received from");
        cell2.setCellStyle(styles.get("header"));
        HSSFCell cell3 = headerRow.createCell(3);
        cell3.setCellValue("Messages sent to");
        cell3.setCellStyle(styles.get("header"));

        int rowIndex = 1;
        long overallReceived = 0;
        long overallSent = 0;

        for (PeriodDO period : periods) {

            HSSFRow periodHeaderRow = sheet.createRow(rowIndex);
            HSSFCell periodHeaderCell = periodHeaderRow.createCell(0);
            periodHeaderCell.setCellValue(period.getPeriod());
            periodHeaderCell.setCellStyle(styles.get("cell_bb_center"));

            rowIndex++;
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + rowIndex + ":$D$" + rowIndex));

            for (PeriodEntryDO entry : period.getEntries()) {
                HSSFRow entryRow = sheet.createRow(rowIndex);
                HSSFCell entryCell0 = entryRow.createCell(0);
                entryCell0.setCellValue(entry.getParty());
                entryCell0.setCellStyle(styles.get("cell_b"));
                HSSFCell entryCell1 = entryRow.createCell(1);
                entryCell1.setCellValue(entry.getService());
                entryCell1.setCellStyle(styles.get("cell_b"));
                HSSFCell entryCell2 = entryRow.createCell(2);
                entryCell2.setCellValue(entry.getReceived());
                entryCell2.setCellStyle(styles.get("cell_b"));
                HSSFCell entryCell3 = entryRow.createCell(3);
                entryCell3.setCellValue(entry.getSent());
                entryCell3.setCellStyle(styles.get("cell_b"));

                rowIndex++;
            }

            HSSFRow summaryRow = sheet.createRow(rowIndex);
            HSSFCell summaryCell0 = summaryRow.createCell(0);
            summaryCell0.setCellValue("Totals");
            summaryCell0.setCellStyle(styles.get("cell_b_right"));

            HSSFCell summaryCell1 = summaryRow.createCell(1);
            summaryCell1.setCellStyle(styles.get("cell_b"));

            rowIndex++;
            sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + rowIndex + ":$B$" + rowIndex));

            HSSFCell summaryCell2 = summaryRow.createCell(2);
            summaryCell2.setCellValue(period.getSumReceived());
            summaryCell2.setCellStyle(styles.get("cell_b"));

            HSSFCell summaryCell3 = summaryRow.createCell(3);
            summaryCell3.setCellValue(period.getSumSent());
            summaryCell3.setCellStyle(styles.get("cell_b"));

            overallReceived += period.getSumReceived();
            overallSent += period.getSumSent();
        }

        HSSFRow summaryRow = sheet.createRow(rowIndex);
        HSSFCell summaryCell0 = summaryRow.createCell(0);
        summaryCell0.setCellValue("Overall Totals");
        summaryCell0.setCellStyle(styles.get("cell_bb_right"));

        HSSFCell summaryCell1 = summaryRow.createCell(1);
        summaryCell1.setCellStyle(styles.get("cell_b"));

        rowIndex++;
        sheet.addMergedRegion(CellRangeAddress.valueOf("$A$" + rowIndex + ":$B$" + rowIndex));

        HSSFCell summaryCell2 = summaryRow.createCell(2);
        summaryCell2.setCellValue(overallReceived);
        summaryCell2.setCellStyle(styles.get("cell_bb"));

        HSSFCell summaryCell3 = summaryRow.createCell(3);
        summaryCell3.setCellValue(overallSent);
        summaryCell3.setCellStyle(styles.get("cell_bb"));

        sheet.setColumnWidth(0, 256 * 10);
        sheet.setColumnWidth(1, 256 * 15);
        sheet.setColumnWidth(2, 256 * 30);
        sheet.setColumnWidth(3, 256 * 30);
    }

    /**
     * create a library of cell styles
     */
    private static Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        CellStyle style;
        Font headerFont = wb.createFont();
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.LIGHT_CORNFLOWER_BLUE.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setFont(headerFont);
        styles.put("header", style);

        Font font1 = wb.createFont();
        font1.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font1);
        styles.put("cell_b", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font1);
        styles.put("cell_b_right", style);

        Font font2 = wb.createFont();
        font2.setColor(IndexedColors.BLUE.getIndex());
        font2.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_LEFT);
        style.setFont(font2);
        styles.put("cell_bb", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFont(font2);
        styles.put("cell_bb_center", style);

        style = createBorderedStyle(wb);
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        style.setFont(font2);
        styles.put("cell_bb_right", style);

        return styles;
    }

    private static CellStyle createBorderedStyle(Workbook wb) {
        CellStyle style = wb.createCellStyle();
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.BLACK.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.BLACK.getIndex());
        return style;
    }

    public String start(AjaxBehaviorEvent abe) {

        return "/pages/reports.xhtml";
    }

    public Date getFromDate() {
        return fromDate;
    }

    public void setFromDate(Date fromDate) {
        this.fromDate = fromDate;
    }

    public Date getToDate() {
        return toDate;
    }

    public void setToDate(Date toDate) {
        this.toDate = toDate;
    }

    public boolean isIncludeEvidences() {
        return includeEvidences;
    }

    public void setIncludeEvidences(boolean includeEvidences) {
        this.includeEvidences = includeEvidences;
    }

    public boolean isReportGenerated() {
        return reportGenerated;
    }

    public void setReportGenerated(boolean reportGenerated) {
        this.reportGenerated = reportGenerated;
    }

    public List<PeriodEntryDO> getResult() {
        return result;
    }

    public void setResult(List<PeriodEntryDO> result) {
        this.result = result;
    }

    public List<PeriodDO> getPeriods() {
        return periods;
    }

    public void setPeriods(List<PeriodDO> periods) {
        this.periods = periods;
    }

    public IDomibusWebAdminReportDao getReportDao() {
        return reportDao;
    }

    public void setReportDao(IDomibusWebAdminReportDao reportDao) {
        this.reportDao = reportDao;
    }
}
