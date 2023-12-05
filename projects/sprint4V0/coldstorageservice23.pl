%====================================================================================
% coldstorageservice23 description   
%====================================================================================
request( storerequest, storerequest(FW) ).
request( initcoldroom, initcoldroom(arg) ).
request( insertticket, insertticket(TICKET) ).
request( chargestatus, chargestatus(arg) ).
dispatch( initdeposit, initdeposit(TICKET) ).
reply( chargetaken, chargetaken(arg) ).  %%for chargestatus
reply( chargefailed, chargefailed(arg) ).  %%for chargestatus
reply( loadaccepted, loadaccepted(TICKET) ).  %%for storerequest
reply( loadrejected, loadrejected(arg) ).  %%for storerequest
reply( coldroom, coldroom(ACTUAL,TEMP) ).  %%for initcoldroom
reply( ticketaccepted, ticketaccepted(arg) ).  %%for insertticket
reply( ticketrejected, ticketrejected(arg) ).  %%for insertticket
request( generateticket, generateticket(FW) ).
reply( ticket, ticket(TICKET) ).  %%for generateticket
request( deposit, deposit(FW) ).
request( depositstatus, depositstatus(arg) ).
reply( chargetakentt, chargetakentt(FW) ).  %%for deposit
reply( chargefailedtt, chargefailedtt(FW) ).  %%for deposit
reply( chargedeposited, chargedeposited(FW) ).  %%for depositstatus
reply( chargedepfailed, chargedeposited(FW) ).  %%for depositstatus
request( move, move(X,Y) ).
request( moveclosest, moveclosest(Xs,Ys) ).
reply( movedone, movedone(arg) ).  %%for move
reply( movefailed, movefailed(arg) ).  %%for move
reply( movecdone, movecdone(arg) ).  %%for moveclosest
reply( movecfailed, movecfailed(arg) ).  %%for moveclosest
dispatch( fail, fail(ERROR) ).
dispatch( sonarstart, sonarstart(arg) ).
dispatch( sonarstop, sonarstop(arg) ).
event( distance, distance(DIST) ).
dispatch( coapUpdate, coapUpdate(RESOURCE,VALUE) ).
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
 qactor( ticketmanager, ctx_coldstorageservice, "it.unibo.ticketmanager.Ticketmanager").
  qactor( transporttrolley, ctx_coldstorageservice, "it.unibo.transporttrolley.Transporttrolley").
  qactor( trolleyexecutor, ctx_coldstorageservice, "it.unibo.trolleyexecutor.Trolleyexecutor").
  qactor( sonarrec, ctx_coldstorageservice, "it.unibo.sonarrec.Sonarrec").
  qactor( coldstorageservice, ctx_coldstorageservice, "it.unibo.coldstorageservice.Coldstorageservice").
  qactor( basicrobot, ctxbasicrobot, "external").
