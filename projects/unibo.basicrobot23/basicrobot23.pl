%====================================================================================
% basicrobot23 description   
%====================================================================================
dispatch( cmd, cmd(MOVE) ).
dispatch( end, end(ARG) ).
request( step, step(TIME) ).
event( sonardata, sonar(DISTANCE) ).
event( obstacle, obstacle(X) ).
request( doplan, doplan(PATH,STEPTIME) ).
dispatch( setrobotstate, setpos(X,Y,D) ).
request( engage, engage(OWNER,STEPTIME) ).
dispatch( disengage, disengage(ARG) ).
request( checkowner, checkowner(CALLER) ).
event( alarm, alarm(X) ).
dispatch( nextmove, nextmove(M) ).
dispatch( nomoremove, nomoremove(M) ).
dispatch( setdirection, dir(D) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
request( getrobotstate, getrobotstate(ARG) ).
%====================================================================================
context(ctxbasicrobot, "localhost",  "TCP", "8020").
 qactor( engager, ctxbasicrobot, "it.unibo.engager.Engager").
  qactor( basicrobot, ctxbasicrobot, "it.unibo.basicrobot.Basicrobot").
  qactor( planexec, ctxbasicrobot, "it.unibo.planexec.Planexec").
  qactor( robotpos, ctxbasicrobot, "it.unibo.robotpos.Robotpos").
