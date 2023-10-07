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
				var actualCurrentColdRoom: Float = 0F
				
				var tempCurrentColdRoom: Float = 0F
				var LastDepositRequested: Float = 0F
				
				var accepted: Boolean = false
				
				val depositQueue: java.util.Queue<Float> = java.util.LinkedList<Float>()
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outblue("CSS: started")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("waiting") { //this:State
					action { //it:State
						CommUtils.outblue("CSS: waiting for new storage request")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="checkAvailability",cond=whenRequest("storerequest"))
					transition(edgeName="t01",targetState="chargeTakenTT",cond=whenReply("chargetakentt"))
					transition(edgeName="t02",targetState="chargeDeposited",cond=whenDispatch("chargedeposited"))
				}	 
				state("checkAvailability") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storerequest(FW)"), Term.createTerm("storerequest(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val FW = payloadArg(0).trim().toFloat()  
								if(  (tempCurrentColdRoom + FW) <= maxColdRoom  
								 ){answer("storerequest", "loadaccepted", "loadaccepted(arg)"   )  
									tempCurrentColdRoom += FW
													LastDepositRequested = FW
													accepted = true
								CommUtils.outblue("CSS: load for $FW KG accepted, currentWeight = $actualCurrentColdRoom")
								}
								else
								 { accepted = false  
								 answer("storerequest", "loadrejected", "loadrejected(arg)"   )  
								 CommUtils.outblue("CSS: load for $FW KG rejected, not enough space in ColdRoom, currentWeight = $actualCurrentColdRoom")
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="requestOrQueueDeposit", cond=doswitchGuarded({ accepted  
					}) )
					transition( edgeName="goto",targetState="waiting", cond=doswitchGuarded({! ( accepted  
					) }) )
				}	 
				state("requestOrQueueDeposit") { //this:State
					action { //it:State
						 accepted = false  
						request("deposit", "deposit($LastDepositRequested)" ,"transporttrolley" )  
						CommUtils.outblue("CSS: requested deposit for $LastDepositRequested KG")
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
						updateResourceRep( "chargetaken"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("chargeDeposited") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("chargedeposited(FW)"), Term.createTerm("chargedeposite(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblue("CSS: load of ${payloadArg(0)} deposited in ColdRoom")
									val FW = payloadArg(0).toFloat()
												actualCurrentColdRoom += FW
												val currentDeposit = depositQueue.remove() 
						}
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
