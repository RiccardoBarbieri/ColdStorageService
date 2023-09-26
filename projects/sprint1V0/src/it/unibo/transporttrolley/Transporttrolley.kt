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
	
class Transporttrolley ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
			val landmarkConf = utils.MapUtils.loadMapConfiguration("servicearea")
				var Anywhere: Pair<Int, Int> = Pair(0,0)
				
				val HomeToIndoorCoord = landmarkConf.getCoordinateClosestToFor("I", Pair(0,0))
				val IndoorToPortCoord = landmarkConf.getCoordinateClosestToFor("P", HomeToIndoorCoord)
				val PortToHomeCoord = landmarkConf.getCoordinateClosestToFor("H", IndoorToPortCoord)
				val PortToIndoorCoord = landmarkConf.getCoordinateClosestToFor("I", IndoorToPortCoord)
				
				val depositQueue: java.util.Queue<Int> = java.util.LinkedList<Int>()
				
				var returnHome: Boolean = false
				
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						CommUtils.outgreen("TT: started")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("moveFailed") { //this:State
					action { //it:State
						CommUtils.outgreen("TT: contact with trolleyexecutor failed on move, try restarting the application")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("somethingFailed") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("fail(ERROR)"), Term.createTerm("fail(ERROR)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblack("TT: TE failed with error -> ${payloadArg(0)}")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("waiting") { //this:State
					action { //it:State
						CommUtils.outgreen("TT: waiting for new deposit request")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t03",targetState="moveToIndoorFromHome",cond=whenDispatch("deposit"))
					transition(edgeName="t04",targetState="somethingFailed",cond=whenDispatch("fail"))
				}	 
				state("queueDeposit") { //this:State
					action { //it:State
							depositQueue.add(0)
						CommUtils.outgreen("TT: added deposit request to queue")
						returnFromInterrupt(interruptedStateTransitions)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("stopAndRestart") { //this:State
					action { //it:State
						CommUtils.outgreen("TT: stopping trolley")
						request("getposition", "getposition(arg)" ,"trolleyexecutor" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t05",targetState="moveToIndoorFromAnywhere",cond=whenReply("position"))
				}	 
				state("moveToIndoorFromAnywhere") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("position(X,Y)"), Term.createTerm("position(X,Y)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									Anywhere = Pair(payloadArg(0).toInt(),payloadArg(1).toInt())
												
												val AnywhereToIndoorCoord = landmarkConf.getCoordinateClosestToFor("I", Anywhere)
												
												val X = AnywhereToIndoorCoord.first
												val Y = AnywhereToIndoorCoord.second
								request("move", "move($X,$Y)" ,"trolleyexecutor" )  
						}
						CommUtils.outgreen("TT: new request, returning to indoor")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t06",targetState="takeCharge",cond=whenReply("movedone"))
					transition(edgeName="t07",targetState="moveFailed",cond=whenReply("movefailed"))
					interrupthandle(edgeName="t08",targetState="queueDeposit",cond=whenDispatch("deposit"),interruptedStateTransitions)
				}	 
				state("moveToIndoorFromHome") { //this:State
					action { //it:State
							val X = HomeToIndoorCoord.first
									val Y = HomeToIndoorCoord.second
						request("move", "move($X,$Y)" ,"trolleyexecutor" )  
						CommUtils.outgreen("TT: moving robot to indoor")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t09",targetState="takeCharge",cond=whenReply("movedone"))
					transition(edgeName="t010",targetState="moveFailed",cond=whenReply("movefailed"))
					interrupthandle(edgeName="t011",targetState="queueDeposit",cond=whenDispatch("deposit"),interruptedStateTransitions)
				}	 
				state("takeCharge") { //this:State
					action { //it:State
						CommUtils.outgreen("TT: loading charge")
						delay(1000) 
						CommUtils.outgreen("TT: charge loaded")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="chargeTaken", cond=doswitch() )
				}	 
				state("chargeTaken") { //this:State
					action { //it:State
						forward("chargetakentt", "chargetakentt" ,"coldstorageservice" ) 
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="toPort", cond=doswitch() )
				}	 
				state("toPort") { //this:State
					action { //it:State
							val X = IndoorToPortCoord.first
									val Y = IndoorToPortCoord.second
						CommUtils.outgreen("TT: moving to access port")
						request("move", "move($X,$Y)" ,"trolleyexecutor" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t012",targetState="depositInColdRoom",cond=whenReply("movedone"))
					transition(edgeName="t013",targetState="moveFailed",cond=whenReply("movefailed"))
					interrupthandle(edgeName="t014",targetState="queueDeposit",cond=whenDispatch("deposit"),interruptedStateTransitions)
				}	 
				state("depositInColdRoom") { //this:State
					action { //it:State
						delay(1000) 
						forward("chargedeposited", "chargedeposited(arg)" ,"coldstorageservice" ) 
						CommUtils.outgreen("TT: charge deposited")
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="checkQueueOrReturn", cond=doswitch() )
				}	 
				state("checkQueueOrReturn") { //this:State
					action { //it:State
							returnHome = (depositQueue.size == 0)  
						if(  returnHome  
						 ){	val X = PortToHomeCoord.first
										val Y = PortToHomeCoord.second
						request("move", "move($X,$Y)" ,"trolleyexecutor" )  
						CommUtils.outgreen("TT: no queued requests, returning to home")
						}
						else
						 {	depositQueue.remove()
						 				val X = PortToIndoorCoord.first
						 				val Y = PortToIndoorCoord.second
						 request("move", "move($X,$Y)" ,"trolleyexecutor" )  
						 CommUtils.outgreen("TT: managing queued request, returning to indoor")
						 }
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t015",targetState="stopAndRestart",cond=whenDispatch("deposit"))
					transition(edgeName="t016",targetState="waiting",cond=whenReplyGuarded("movedone",{ returnHome  
					}))
					transition(edgeName="t017",targetState="takeCharge",cond=whenReplyGuarded("movedone",{ !returnHome  
					}))
					transition(edgeName="t018",targetState="moveFailed",cond=whenReply("movefailed"))
				}	 
			}
		}
}
