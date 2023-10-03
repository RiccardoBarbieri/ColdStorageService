%====================================================================================
% testsag description   
%====================================================================================
request( storerequest, storerequest(FW) ).
dispatch( chargetakentt, chargetaken(arg) ).
dispatch( deposit, deposit(arg) ).
%====================================================================================
context(ctx_coldstorageservice, "localhost",  "TCP", "8020").
 qactor( coldstorageservice, ctx_coldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctx_coldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
