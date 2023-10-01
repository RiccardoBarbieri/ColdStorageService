%====================================================================================
% mapemptyroom23 description   
%====================================================================================
dispatch( cmd, cmd(MOVE) ).
request( step, step(TIME) ).
event( alarm, alarm(X) ).
request( engage, engage(OWNER,STEPTIME) ).
dispatch( disengage, disengage(ARG) ).
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctxmapemptyroom23, "localhost",  "TCP", "8032").
 qactor( basicrobot, ctxbasicrobot, "external").
  qactor( mapemptyroom23, ctxmapemptyroom23, "it.unibo.mapemptyroom23.Mapemptyroom23").
