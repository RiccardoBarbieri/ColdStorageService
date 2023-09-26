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
				var accepted: Boolean = false
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
					interrupthandle(edgeName="t01",targetState="finalizeDeposit",cond=whenDispatch("chargetakentt"),interruptedStateTransitions)
					interrupthandle(edgeName="t02",targetState="chargeDeposited",cond=whenDispatch("chargedeposited"),interruptedStateTransitions)
				}	 
				state("checkAvailability") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("storerequest(FW)"), Term.createTerm("storerequest(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 val FW = payloadArg(0).trim().toFloat()  
								if(  (currentColdRoom + FW) <= maxColdRoom  
								 ){answer("storerequest", "loadaccepted", "loadaccepted(arg)"   )  
								CommUtils.outblue("CSS: load for $FW KG accepted, currentWeight = $currentColdRoom")
									currentColdRoom += FW
													accepted = true	
								}
								else
								 { accepted = false  
								 answer("storerequest", "loadrejected", "loadrejected(arg)"   )  
								 CommUtils.outblue("CSS: load for $FW KG rejected, not enough space in ColdRoom, currentWeight = $currentColdRoom")
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="forwardDeposit", cond=doswitchGuarded({ accepted  
					}) )
					transition( edgeName="goto",targetState="waiting", cond=doswitchGuarded({! ( accepted  
					) }) )
				}	 
				state("forwardDeposit") { //this:State
					action { //it:State
						 accepted = false  
						forward("deposit", "deposit(arg)" ,"transporttrolley" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("finalizeDeposit") { //this:State
					action { //it:State
						CommUtils.outblue("CSS: charge taken")
						updateResourceRep( "chargetaken"  
						)
						returnFromInterrupt(interruptedStateTransitions)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("chargeDeposited") { //this:State
					action { //it:State
						CommUtils.outblue("CSS: current load deposited in ColdRoom, currentWeight = $currentColdRoom")
						returnFromInterrupt(interruptedStateTransitions)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
			}
		}
}
