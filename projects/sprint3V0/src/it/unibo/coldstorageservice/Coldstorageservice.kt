/* Generated by AN DISI Unibo */ 
package it.unibo.coldstorageservice

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import it.unibo.kactor.sysUtil.createActor   //Sept2023
class Coldstorageservice ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
			val maxColdRoom: Float = 10F
				var ActualCurrentColdRoom: Float = 0F
				
				var TempCurrentColdRoom: Float = 0F
				var LastDepositRequested: Float = 0F
				
				var accepted: Boolean = false
				
				val weightTicketMap = mutableMapOf<String, Float>()
				
				val DLIMT = 20
				return { //this:ActionBasciFsm
				state("chargeFailed") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("chargefailedtt(FW)"), Term.createTerm("chargefailedtt(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblue("CSS: TransportTrolley failed to take the current charge (${payloadArg(0)} KG)")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("depositFailed") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("chargedeposited(FW)"), Term.createTerm("chargedepfailed(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblue("CSS: TransportTrolley failed to deposit the current charge (${payloadArg(0)} KG)")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("s0") { //this:State
					action { //it:State
						delegate("insertticket", "ticketmanager") 
						delegate("sonarstop", "trolleyexecutor") 
						delegate("sonarstart", "trolleyexecutor") 
						delegate("distance", "sonarrec") 
						CoapObserverSupport(myself, "localhost","8021","ctx_coldstorageservice","transporttrolley")
						CommUtils.outblue("CSS: started")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("sendColdRoom") { //this:State
					action { //it:State
						answer("initcoldroom", "coldroom", "coldroom($ActualCurrentColdRoom,$TempCurrentColdRoom)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("forwardUpdate") { //this:State
					action { //it:State
						CommUtils.outcyan("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						if( checkMsgContent( Term.createTerm("coapUpdate(RESOURCE,VALUE)"), Term.createTerm("coapUpdate(RES,VAL)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								updateResourceRep( payloadArg(1)  
								)
						}
						returnFromInterrupt(interruptedStateTransitions)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("waiting") { //this:State
					action { //it:State
						CommUtils.outblue("CSS: waiting for new storage request")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t040",targetState="checkAvailability",cond=whenRequest("storerequest"))
					transition(edgeName="t041",targetState="requestDeposit",cond=whenDispatch("initdeposit"))
					transition(edgeName="t042",targetState="chargeTakenTT",cond=whenReply("chargetakentt"))
					transition(edgeName="t043",targetState="chargeFailed",cond=whenReply("chargefailedtt"))
					transition(edgeName="t044",targetState="chargeDeposited",cond=whenReply("chargedeposited"))
					transition(edgeName="t045",targetState="depositFailed",cond=whenReply("chargedepfailed"))
					transition(edgeName="t046",targetState="sendColdRoom",cond=whenRequest("initcoldroom"))
					interrupthandle(edgeName="t047",targetState="forwardUpdate",cond=whenDispatch("coapUpdate"),interruptedStateTransitions)
				}	 
				state("checkAvailability") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storerequest(FW)"), Term.createTerm("storerequest(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val FW = payloadArg(0).trim().toFloat()  
								if(  (TempCurrentColdRoom + FW) <= maxColdRoom  
								 ){	TempCurrentColdRoom += FW
													LastDepositRequested = FW
													accepted = true
								CommUtils.outblue("CSS: load for $FW KG accepted, currentWeight = $ActualCurrentColdRoom")
								}
								else
								 { accepted = false  
								 answer("storerequest", "loadrejected", "loadrejected(arg)"   )  
								 CommUtils.outblue("CSS: load for $FW KG rejected, not enough space in ColdRoom, currentWeight = $ActualCurrentColdRoom")
								 }
						}
						updateResourceRep( "tempcurrentcoldroom($TempCurrentColdRoom)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="requestTicket", cond=doswitchGuarded({ accepted  
					}) )
					transition( edgeName="goto",targetState="waiting", cond=doswitchGuarded({! ( accepted  
					) }) )
				}	 
				state("requestTicket") { //this:State
					action { //it:State
						request("generateticket", "generateticket($LastDepositRequested)" ,"ticketmanager" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t048",targetState="replyTicket",cond=whenReply("ticket"))
				}	 
				state("replyTicket") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("ticket(TICKET)"), Term.createTerm("ticket(TICKET)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val Ticket = payloadArg(0)  
								CommUtils.outblue("CSS: Serving ticket")
								answer("storerequest", "loadaccepted", "loadaccepted($Ticket)"   )  
								 weightTicketMap[Ticket] = LastDepositRequested  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("requestDeposit") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("initdeposit(TICKET)"), Term.createTerm("initdeposit(TICKET)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									LastDepositRequested = weightTicketMap[payloadArg(0)]!!
												weightTicketMap.remove(payloadArg(0))
								request("deposit", "deposit($LastDepositRequested)" ,"transporttrolley" )  
								CommUtils.outblue("CSS: requested deposit for $LastDepositRequested KG")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("chargeTakenTT") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("chargetakentt(FW)"), Term.createTerm("chargetakentt(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblue("CSS: charge of ${payloadArg(0)} taken")
						}
						request("depositstatus", "depositstatus(arg)" ,"transporttrolley" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t049",targetState="replyChargeStatus",cond=whenRequest("chargestatus"))
				}	 
				state("replyChargeStatus") { //this:State
					action { //it:State
						answer("chargestatus", "chargetaken", "chargetaken(arg)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("chargeDeposited") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("chargedeposited(FW)"), Term.createTerm("chargedeposited(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									val FW = payloadArg(0).toFloat()
												ActualCurrentColdRoom += FW
								CommUtils.outblue("CSS: load of ${payloadArg(0)} deposited in ColdRoom, current weight = $ActualCurrentColdRoom")
						}
						updateResourceRep( "actualcurrentcoldroom($ActualCurrentColdRoom)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
			}
		}
} 
