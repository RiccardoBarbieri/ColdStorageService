%====================================================================================
% coldstorageservice23 description   
%====================================================================================
request( storerequest, storerequest(FW) ).
request( codeentered, codeentered(TICKET) ).
event( rejrequpdate, rejrequpdate(NREJ) ).
dispatch( newcharge, newcharge(FW) ).
dispatch( chargetakentt, chargetakentt(arg) ).
dispatch( chargetaken, chargetaken(TICKET) ).
dispatch( chargedeposited, chargedeposited(FW) ).
event( coldroomupdate, coldroomupdate(KG) ).
event( statusupdate, statusupdate(POS,STATE) ).
dispatch( stop, stop(arg) ).
dispatch( resume, resume(arg) ).
dispatch( goToINDOOR, goToINDOOR(arg) ).
dispatch( goToColdRoom, goToColdRoom(arg) ).
dispatch( goToHOME, goToHOME(arg) ).
dispatch( arrivedINDOOR, arrivedINDOOR(arg) ).
dispatch( arrivedColdRoom, arrivedColdRoom(arg) ).
dispatch( arrivedHOME, arrivedHOME(arg) ).
%====================================================================================
context(ctx_coldstorageservice, "localhost",  "TCP", "8021").
context(ctx_access, "localhost",  "TCP", "8022").
context(ctx_statusmonitor, "localhost",  "TCP", "8023").
context(ctx_alarmsystem, "localhost",  "TCP", "8024").
context(ctx_basicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( coldstorageservice, ctx_coldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( basicrobotsim, ctx_basicrobot, "it.unibo.basicrobotsim.Basicrobotsim").
  qactor( transporttrolley, ctx_coldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( serviceaccessgui, ctx_access, "it.unibo.serviceaccessgui.Serviceaccessgui").
  qactor( servicestatusgui, ctx_statusmonitor, "it.unibo.servicestatusgui.Servicestatusgui").
  qactor( warningdevice, ctx_alarmsystem, "it.unibo.warningdevice.Warningdevice").
  qactor( alarmdevice, ctx_alarmsystem, "it.unibo.alarmdevice.Alarmdevice").
