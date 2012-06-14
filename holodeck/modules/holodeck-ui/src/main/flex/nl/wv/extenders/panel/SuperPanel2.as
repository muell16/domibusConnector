package nl.wv.extenders.panel
{
  import mx.containers.Panel;
  import mx.controls.Button;
  import mx.core.UIComponent;
  import mx.core.Application;
  import mx.core.IUIComponent;
  import mx.events.FlexEvent;
  import mx.events.EffectEvent;
  import mx.effects.Resize;
  import mx.managers.CursorManager;
   
  import flash.geom.Point;
  import flash.geom.Rectangle;
  import flash.events.MouseEvent;
  import mx.core.UITextField;
	
	
  public class SuperPanel2 extends Panel 
  {
        [Bindable] public var showControls:Boolean = false;
        [Bindable] public var enableResize:Boolean = false;
                
        [Embed(source="/assets/img/resizeCursor.png")]
        private static var resizeCursor:Class;
        
        private var    pTitleBar:UIComponent;
        private var oW:Number;
        private var oH:Number;
        private var oX:Number;
        private var oY:Number;
        private var closeButton:Button        = new Button();
        private var upDownButton:Button        = new Button();
        private var resizeHandler:Button    = new Button();
        private var upMotion:Resize            = new Resize();
        private var downMotion:Resize        = new Resize();
        private var resizeCur:Number        = 0;
        private var oPoint:Point             = new Point();
        
        public function SuperPanel2() {}
        
        override protected function createChildren():void {
            super.createChildren();
            this.pTitleBar = super.titleBar;
            this.setStyle("borderColor", 0xFDF6D1);
                        
            if (enableResize) {
                this.resizeHandler.width     = 12;
                this.resizeHandler.height    = 12;
                this.resizeHandler.styleName = "resizeHndlr";
                this.rawChildren.addChild(resizeHandler);
                this.initPos();
            }
            
            if (showControls) {                
                this.closeButton.width     = 10;
                this.closeButton.height    = 10;
                this.closeButton.styleName = "closeBtn";
                this.pTitleBar.addChild(this.closeButton);
                this.upDownButton.width     = 10;
                this.upDownButton.height    = 10;
                this.upDownButton.styleName = "decreaseBtn";
                this.pTitleBar.addChild(this.upDownButton);
            }
            
            this.positionChildren();    
            this.addListeners();
        }
    
        public function initPos():void {
            this.oW = this.width;
            this.oH = this.height;
            this.oX = this.x;
            this.oY = this.y;
        }
    
        public function positionChildren():void {
            if (showControls) {
                this.closeButton.buttonMode     = true;
                this.closeButton.useHandCursor  = true;
                this.closeButton.y                 = 8;
                this.closeButton.x                 = this.unscaledWidth - this.closeButton.width - 8;
                this.upDownButton.buttonMode    = true;
                this.upDownButton.useHandCursor = true;
                this.upDownButton.y             = 8;
                this.upDownButton.x             = this.unscaledWidth - this.upDownButton.width - 24;
            }
            
            if (enableResize) {
                this.resizeHandler.y = this.unscaledHeight - resizeHandler.height - 1;
                this.resizeHandler.x = this.unscaledWidth - resizeHandler.width - 1;
            }
        }
                
        public function addListeners():void {
            this.pTitleBar.addEventListener(MouseEvent.MOUSE_DOWN, dragStartEventHandler);
            this.pTitleBar.addEventListener(MouseEvent.MOUSE_UP, dragDropEventHandler);
            
            if (showControls) {
                this.closeButton.addEventListener(MouseEvent.CLICK, closeEventHandler);
                this.upDownButton.addEventListener(MouseEvent.CLICK, upDownEventHandler);
            }
            
            if (enableResize) {
                this.resizeHandler.addEventListener(MouseEvent.MOUSE_OVER, mouseOverEventHandler);
                this.resizeHandler.addEventListener(MouseEvent.MOUSE_OUT, mouseOutEventHandler);
                this.resizeHandler.addEventListener(MouseEvent.MOUSE_DOWN, mouseDownEventHandler);
            }
        }
                
        public function dragStartEventHandler(event:MouseEvent):void {
            Application.application.parent.addEventListener(MouseEvent.MOUSE_UP, dragDropEventHandler);
            this.parent.setChildIndex(this, this.parent.numChildren - 1);
            this.setStyle("headerColors", [0xFDF6D1,0xFDF9E3]);
            this.startDrag(false, new Rectangle(0, 0, screen.width - this.width, screen.height - this.height));
        }
        
        public function dragDropEventHandler(event:MouseEvent):void {
            this.setStyle("headerColors", null);
            this.stopDrag();
        }
        
        public function upDownEventHandler(event:MouseEvent):void {
            Application.application.parent.removeEventListener(MouseEvent.MOUSE_UP, mouseUpEventHandler);
            this.upMotion.target = this;
            this.upMotion.duration = 300;
            this.upMotion.heightFrom = oH;
            this.upMotion.heightTo = 28;
            this.upMotion.end();
            
            this.downMotion.target = this;
            this.downMotion.duration = 300;
            this.downMotion.heightFrom = 28;
            this.downMotion.heightTo = oH;
            this.downMotion.end();
            
            if (this.height == oH) {
                this.initPos();
                this.upMotion.play();
                this.resizeHandler.visible = false;
                this.upDownButton.styleName = "increaseBtn";
            } else if (this.height == 28) {
                this.downMotion.play();
                this.downMotion.addEventListener(EffectEvent.EFFECT_END, endEffectEventHandler);
                //this.resizeHandler.visible = true;
                this.upDownButton.styleName = "decreaseBtn";
            }
        }
        
        public function endEffectEventHandler(event:EffectEvent):void {
            this.resizeHandler.visible = true;
        }
        
        public function closeEventHandler(event:MouseEvent):void {
            Application.application.removeChild(this);
        }
        
        public function mouseOverEventHandler(event:MouseEvent):void {
            this.resizeCur = CursorManager.setCursor(resizeCursor);
        }
        
        public function mouseOutEventHandler(event:MouseEvent):void {
            CursorManager.removeCursor(CursorManager.currentCursorID);
        }
        
        public function mouseDownEventHandler(event:MouseEvent):void {
            Application.application.parent.addEventListener(MouseEvent.MOUSE_MOVE, mouseMoveEventHandler);
            Application.application.parent.addEventListener(MouseEvent.MOUSE_UP, mouseUpEventHandler);
            this.resizeHandler.addEventListener(MouseEvent.MOUSE_OVER, mouseOverEventHandler);
            this.resizeCur = CursorManager.setCursor(resizeCursor);
            this.oPoint.x = mouseX;
            this.oPoint.y = mouseY;
            this.oPoint = this.localToGlobal(oPoint);        
        }
        
        public function mouseMoveEventHandler(event:MouseEvent):void {
            this.stopDragging();
            
            var xPlus:Number = Application.application.parent.mouseX - this.oPoint.x;
            var yPlus:Number = Application.application.parent.mouseY - this.oPoint.y;
            
            if (this.oW + xPlus > 140) {
                this.width = this.oW + xPlus;
            }
            
            if (this.oH + yPlus > 80) {
                this.height = this.oH + yPlus;
            }
            this.positionChildren();
        }
        
        public function mouseUpEventHandler(event:MouseEvent):void {
            Application.application.parent.removeEventListener(MouseEvent.MOUSE_MOVE, mouseMoveEventHandler);
            CursorManager.removeCursor(CursorManager.currentCursorID);
            this.resizeHandler.addEventListener(MouseEvent.MOUSE_OVER, mouseOverEventHandler);
            this.initPos();
        }
    } 
}