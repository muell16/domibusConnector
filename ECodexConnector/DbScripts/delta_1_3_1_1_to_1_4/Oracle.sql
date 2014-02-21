alter table ecodex_party add PARTY_ID_TYPE varchar2(50);

update ecodex_party set party_id_type='urn:oasis:names:tc:ebcore:partyid-type:iso3166-1';

commit;