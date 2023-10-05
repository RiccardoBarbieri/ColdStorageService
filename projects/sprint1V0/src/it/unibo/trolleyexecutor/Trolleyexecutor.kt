/* Generated by AN DISI Unibo */ 
package it.unibo.trolleyexecutor

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Trolleyexecutor ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
			var isMoving = false
				var destinations = mutableListOf<Pair<Int,Int>>()
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outmagenta("TE: trolleyexecutor initialized")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="engage", cond=doswitch() )
				}	 
				state("engage") { //this:State
					action { //it:State
						request("engage", "engage(trolleyexecutor,300)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_engage", 
				 	 					  scope, context!!, "local_tout_trolleyexecutor_engage", 10000.toLong() )
					}	 	 
					 transition(edgeName="t018",targetState="engageFail",cond=whenTimeout("local_tout_trolleyexecutor_engage"))   
					transition(edgeName="t019",targetState="setState",cond=whenReply("engagedone"))
					transition(edgeName="t020",targetState="engageFail",cond=whenReply("engagerefused"))
				}	 
				state("engageFail") { //this:State
					action { //it:State
						 val ErrorString = "Ingaggio robot fallito"  
						forward("fail", "fail($ErrorString)" ,"transporttrolley" ) 
						CommUtils.outmagenta("TE: engage with robot failed, try restarting the application")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("moveFail") { //this:State
					action { //it:State
						answer("move", "movefailed", "movecfailed(arg)"   )  
						CommUtils.outmagenta("TE: moverobot failed, try restarting the application")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("movecFail") { //this:State
					action { //it:State
						answer("move", "movefailed", "movecfailed(arg)"   )  
						CommUtils.outmagenta("TE: moverobot failed, try restarting the application")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("timeout") { //this:State
					action { //it:State
						CommUtils.outmagenta("TE: connection with basicrobot timed out")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("setState") { //this:State
					action { //it:State
						forward("setrobotstate", "setpos(0,0,d)" ,"basicrobot" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("waiting") { //this:State
					action { //it:State
						CommUtils.outmagenta("TE: waiting")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t021",targetState="move",cond=whenRequest("move"))
					transition(edgeName="t022",targetState="stop",cond=whenRequest("moveclosest"))
				}	 
				state("move") { //this:State
					action { //it:State
						CommUtils.outmagenta("TE: move")
						if( checkMsgContent( Term.createTerm("move(X,Y)"), Term.createTerm("move(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									val X = payloadArg(0)
												val Y = payloadArg(1)
								request("moverobot", "moverobot($X,$Y)" ,"basicrobot" )  
								 isMoving = true  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_move", 
				 	 					  scope, context!!, "local_tout_trolleyexecutor_move", 30000.toLong() )
					}	 	 
					 transition(edgeName="t023",targetState="timeout",cond=whenTimeout("local_tout_trolleyexecutor_move"))   
					transition(edgeName="t024",targetState="moveCompleted",cond=whenReply("moverobotdone"))
					transition(edgeName="t025",targetState="moveFail",cond=whenReply("moverobotfailed"))
				}	 
				state("stop") { //this:State
					action { //it:State
						CommUtils.outmagenta("TE: moveClosest")
						if( checkMsgContent( Term.createTerm("moveclosest(Xs,Ys)"), Term.createTerm("moveclosest(Xs,Ys)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								 destinations = utils.PosUtils.destStringListToPairs(payloadArg(0), payloadArg(1))  
								if(  isMoving  
								 ){emit("alarm", "alarm(arg)" ) 
								 isMoving = false  
								}
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="askPosition", cond=doswitch() )
				}	 
				state("askPosition") { //this:State
					action { //it:State
						CommUtils.outblack("TE: askPosition")
						request("getrobotstate", "getrobotstate(arg)" ,"basicrobot" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_askPosition", 
				 	 					  scope, context!!, "local_tout_trolleyexecutor_askPosition", 30000.toLong() )
					}	 	 
					 transition(edgeName="t026",targetState="timeout",cond=whenTimeout("local_tout_trolleyexecutor_askPosition"))   
					transition(edgeName="t027",targetState="moveClosest",cond=whenReply("robotstate"))
				}	 
				state("moveClosest") { //this:State
					action { //it:State
						CommUtils.outblack("TE: moveClosest")
						if( checkMsgContent( Term.createTerm("robotstate(POS,DIR)"), Term.createTerm("robotstate(POS,DIR)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									val position = utils.PosUtils.posStringToPair(payloadArg(0).trim())
												
												val closest = utils.PosUtils.closestDestination(position, destinations)
												val X = closest.first
												val Y = closest.second
								request("moverobot", "moverobot($X,$Y)" ,"basicrobot" )  
								 isMoving = true  
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_moveClosest", 
				 	 					  scope, context!!, "local_tout_trolleyexecutor_moveClosest", 30000.toLong() )
					}	 	 
					 transition(edgeName="t028",targetState="timeout",cond=whenTimeout("local_tout_trolleyexecutor_moveClosest"))   
					transition(edgeName="t029",targetState="movecCompleted",cond=whenReply("moverobotdone"))
					transition(edgeName="t030",targetState="movecFail",cond=whenReply("moverobotfailed"))
				}	 
				state("moveCompleted") { //this:State
					action { //it:State
						 isMoving = false  
						CommUtils.outmagenta("TE: moveCompleted")
						answer("move", "movedone", "movedone(arg)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("movecCompleted") { //this:State
					action { //it:State
						 isMoving = false  
						CommUtils.outmagenta("TE: movecCompleted")
						answer("moveclosest", "movecdone", "movecdone(arg)"   )  
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
