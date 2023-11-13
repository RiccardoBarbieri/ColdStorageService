%====================================================================================
% coldstorageservice23 description   
%====================================================================================
request( storerequest, storerequest(FW) ).
request( chargestatus, chargestatus(arg) ).
reply( loadaccepted, loadaccepted(arg) ).  %%for storerequest
reply( loadrejected, loadrejected(arg) ).  %%for storerequest
reply( chargetaken, chargetaken(arg) ).  %%for chargestatus
reply( chargefailed, chargefailed(arg) ).  %%for chargestatus
request( toindoor, toindoor(arg) ).
request( depositcharge, depositcharge(arg) ).
request( returnhome, returnhome(arg) ).
reply( chargetakentt, chargetakentt(FW) ).  %%for toindoor
reply( chargedeposited, chargedeposited(FW) ).  %%for depositcharge
reply( returned, returned(FW) ).  %%for returnhome
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctx_coldstorageservice, "localhost",  "TCP", "8021").
context(ctx_access, "localhost",  "TCP", "8022").
 qactor( serviceaccessgui, ctx_access, "it.unibo.serviceaccessgui.Serviceaccessgui").
  qactor( coldstorageservice, ctx_coldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctx_coldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( basicrobot, ctxbasicrobot, "external").
