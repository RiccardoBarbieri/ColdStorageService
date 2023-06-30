%====================================================================================
% coldstorageservice23 description   
%====================================================================================
context(ctx_coldstorageservice, "localhost",  "TCP", "8021").
context(ctx_access, "localhost",  "TCP", "8022").
context(ctx_statusmonitor, "localhost",  "TCP", "8023").
context(ctx_alarmsystem, "localhost",  "TCP", "8024").
context(ctx_basicrobot, "127.0.0.1",  "TCP", "8020").
 qactor( basicrobot, ctx_basicrobot, "external").
  qactor( coldstorageservice, ctx_coldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctx_coldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( serviceaccessgui, ctx_access, "it.unibo.serviceaccessgui.Serviceaccessgui").
  qactor( servicestatusgui, ctx_statusmonitor, "it.unibo.servicestatusgui.Servicestatusgui").
  qactor( warningdevice, ctx_alarmsystem, "it.unibo.warningdevice.Warningdevice").
  qactor( alarmdevice, ctx_alarmsystem, "it.unibo.alarmdevice.Alarmdevice").
