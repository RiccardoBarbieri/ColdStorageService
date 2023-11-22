/* Generated by AN DISI Unibo */ 
package it.unibo.transporttrolley

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
class Transporttrolley ( name: String, scope: CoroutineScope, isconfined: Boolean=false  ) : ActorBasicFsm( name, scope, confined=isconfined ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
			var MovementState: MState = MState.HOME
				var ServiceState: SState = SState.WAITING
				
				var ServingNow: Float = 0f
				
				var Pos: Pair<Int,Int> = Pair(0,0)
				var ChargeList : MutableList<Float> = mutableListOf<Float>()
				return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
							ServiceState = SState.WAITING
									MovementState = MState.HOME
						emit("statusupdate", "statusupdate(pos(N,N),$MovementState)" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t09",targetState="addCharge",cond=whenDispatch("newcharge"))
				}	 
				state("addCharge") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("newcharge(FW)"), Term.createTerm("newcharge(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 ChargeList.add(payloadArg(1).toFloat())  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="handleCharge", cond=doswitch() )
				}	 
				state("handleCharge") { //this:State
					action { //it:State
						if(  ServiceState == SState.WAITING && !ChargeList.isEmpty()  
						 ){	ServiceState = SState.SERVING
										MovementState = MState.MOVING
						emit("statusupdate", "statusupdate(pos(N,N),$MovementState)" ) 
						forward("goToINDOOR", "goToINDOOR(arg)" ,"basicrobotsim" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t010",targetState="takeCharge",cond=whenDispatch("arrivedINDOOR"))
					interrupthandle(edgeName="t011",targetState="addCharge",cond=whenDispatch("newcharge"),interruptedStateTransitions)
				}	 
				state("takeCharge") { //this:State
					action { //it:State
						if(  ServingNow == 0f  
						 ){CommUtils.outblack("TT  | Charge taken from truck")
						 ServingNow = ChargeList.removeAt(0)  
						forward("goToColdRoom", "goToColdRoom(arg)" ,"basicrobotsim" ) 
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t012",targetState="notifyDeposit",cond=whenDispatch("arrivedColdRoom"))
					interrupthandle(edgeName="t013",targetState="addCharge",cond=whenDispatch("newcharge"),interruptedStateTransitions)
				}	 
				state("notifyDeposit") { //this:State
					action { //it:State
						forward("chargedeposited", "chargedeposited($ServingNow)" ,"coldstorageservice" ) 
						 ServingNow = 0f  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t014",targetState="work",cond=whenDispatch("arrivedHOME"))
					interrupthandle(edgeName="t015",targetState="addCharge",cond=whenDispatch("newcharge"),interruptedStateTransitions)
				}	 
				state("stopped") { //this:State
					action { //it:State
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t016",targetState="handleCharge",cond=whenDispatch("resume"))
				}	 
			}
		}
} 
