%====================================================================================
% coldstorageservice23 description   
%====================================================================================
request( storerequest, storerequest(FW) ).
reply( loadaccepted, loadaccepted(arg) ).  %%for storerequest
reply( loadrejected, loadrejected(arg) ).  %%for storerequest
dispatch( chargetaken, chargetaken(arg) ).
dispatch( chargetakentt, chargetaken(arg) ).
dispatch( chargedeposited, chargedeposited(arg) ).
dispatch( deposit, deposit(arg) ).
request( move, move(X,Y) ).
request( getposition, getposition(arg) ).
dispatch( stop, stop(arg) ).
reply( movedone, movedone(arg) ).  %%for move
reply( movefailed, movefailed(arg) ).  %%for move
reply( position, position(X,Y) ).  %%for getposition
dispatch( fail, fail(ERROR) ).
request( engage, engage(OWNER,STEPTIME) ).
reply( engagedone, engagedone(ARG) ).  %%for engage
reply( engagerefused, engagerefused(ARG) ).  %%for engage
dispatch( disengage, disengage(ARG) ).
request( moverobot, moverobot(TARGETX,TARGETY) ).
reply( moverobotdone, moverobotok(ARG) ).  %%for moverobot
reply( moverobotfailed, moverobotfailed(PLANDONE,PLANTODO) ).  %%for moverobot
dispatch( setrobotstate, setpos(X,Y,D) ).
event( alarm, alarm(X) ).
request( getrobotstate, getrobotstate(ARG) ).
reply( robotstate, robotstate(POS,DIR) ).  %%for getrobotstate
%====================================================================================
context(ctxbasicrobot, "127.0.0.1",  "TCP", "8020").
context(ctx_coldstorageservice, "localhost",  "TCP", "8021").
 qactor( coldstorageservice, ctx_coldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( transporttrolley, ctx_coldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( trolleyexecutor, ctx_coldstorageservice, "it.unibo.trolleyexecutor.Trolleyexecutor").
  qactor( basicrobot, ctxbasicrobot, "external").
