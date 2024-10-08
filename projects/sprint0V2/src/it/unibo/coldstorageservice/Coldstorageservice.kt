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
	
class Coldstorageservice ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
			val maxColdRoom: Float = 10F
				var currentColdRoom: Float = 0F
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
						CommUtils.outblue("CSS: waiting for requests")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t00",targetState="checkAvailability",cond=whenRequest("storerequest"))
					transition(edgeName="t01",targetState="handleDeposit",cond=whenRequest("deposit"))
					transition(edgeName="t02",targetState="sendChargeTaken",cond=whenRequest("chargestatus"))
				}	 
				state("checkAvailability") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storerequest(arg)"), Term.createTerm("storerequest(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val FW = payloadArg(0).trim().toFloat()  
								if(  (currentColdRoom + FW) <= maxColdRoom  
								 ){answer("storerequest", "loadaccepted", "loadaccepted(_)"   )  
								CommUtils.outblue("CSS: load for $FW KG accepted, currentWeight = $currentColdRoom")
									currentColdRoom += FW   
								}
								else
								 {answer("storerequest", "loadrejected", "loadrejected(_)"   )  
								 CommUtils.outblue("CSS: load for $FW KG rejected, not enough space in ColdRoom, currentWeight = $currentColdRoom")
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("sendChargeTaken") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("chargestatus(arg)"), Term.createTerm("chargestatus(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								answer("chargestatus", "chargetaken", "chargetaken(_)"   )  
								CommUtils.outblue("CSS: charge taken")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("handleDeposit") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("deposit(arg)"), Term.createTerm("deposit(_)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  true  
								 ){answer("deposit", "ticketaccepted", "ticketaccepted(_)"   )  
								CommUtils.outblue("CSS: ticket accepted")
								}
								else
								 {answer("deposit", "ticketrejected", "ticketrejected(_)"   )  
								 CommUtils.outblue("CSS: ticket rejected")
								 }
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
