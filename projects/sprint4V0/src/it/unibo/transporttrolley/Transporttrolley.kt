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
			val landmarkConf = utils.MapUtils.loadMapConfiguration("servicearea")
				var Anywhere: Pair<Int, Int> = Pair(0,0)
				
				val HomeToIndoorCoord = landmarkConf.getCoordinateClosestToFor("I", Pair(0,0))
				val IndoorToPortCoord = landmarkConf.getCoordinateClosestToFor("P", HomeToIndoorCoord)
				val PortToHomeCoord = landmarkConf.getCoordinateClosestToFor("H", IndoorToPortCoord)
				val PortToIndoorCoord = landmarkConf.getCoordinateClosestToFor("I", IndoorToPortCoord)
				
				var CurrentLoad: String = ""
				
				var isMoving: Boolean = false
				var wasMoving: Boolean = false
				
				var lastState: String = ""
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
				state("sonarInterrupt") { //this:State
					action { //it:State
						discardMessages = true
						CommUtils.outgreen("TT: interrupted by sonar")
						if(  isMoving  
						 ){ wasMoving = true  
						}
						else
						 { wasMoving = false  
						 }
						forward("stop", "stop(arg)" ,"trolleyexecutor" ) 
						 isMoving = false  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t02",targetState="waiting",cond=whenDispatchGuarded("sonarstart",{ lastState == "waiting"  
					}))
					transition(edgeName="t03",targetState="returnHome",cond=whenDispatchGuarded("sonarstart",{ lastState == "returnHome"  
					}))
					transition(edgeName="t04",targetState="restartToIndoor",cond=whenDispatchGuarded("sonarstart",{ lastState == "restartToIndoor"  
					}))
					transition(edgeName="t05",targetState="moveToIndoorFromHome",cond=whenDispatchGuarded("sonarstart",{ lastState == "moveToIndoorFromHome"  
					}))
					transition(edgeName="t06",targetState="toPort",cond=whenDispatchGuarded("sonarstart",{ lastState == "toPort"  
					}))
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
				state("chargeTakeFailed") { //this:State
					action { //it:State
						answer("deposit", "chargefailedtt", "chargefailedtt($CurrentLoad)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="moveFailed", cond=doswitch() )
				}	 
				state("chargeDepositFailed") { //this:State
					action { //it:State
						answer("depositstatus", "chargedepfailed", "chargedepfailed($CurrentLoad)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="moveFailed", cond=doswitch() )
				}	 
				state("somethingFailed") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("fail(ERROR)"), Term.createTerm("fail(ERROR)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outgreen("TT: TE failed with error -> ${payloadArg(0)}")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
				}	 
				state("waiting") { //this:State
					action { //it:State
						 lastState = "waiting"  
						CommUtils.outgreen("TT: waiting for new deposit request")
						updateResourceRep( "ttstate(stopped,home)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t07",targetState="moveToIndoorFromHome",cond=whenRequest("deposit"))
					transition(edgeName="t08",targetState="somethingFailed",cond=whenDispatch("fail"))
					transition(edgeName="t09",targetState="giveStateTT",cond=whenRequest("initstatett"))
				}	 
				state("giveStateTT") { //this:State
					action { //it:State
						answer("initstatett", "statett", "statett(STOPPED,HOME)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="waiting", cond=doswitch() )
				}	 
				state("returnHome") { //this:State
					action { //it:State
						 lastState = "returnHome"  
						CommUtils.outgreen("TT: returning HOME")
							val X = PortToHomeCoord.first
									val Y = PortToHomeCoord.second
						request("move", "move($X,$Y)" ,"trolleyexecutor" )  
						updateResourceRep( "ttstate(moving,home)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t010",targetState="waiting",cond=whenReply("movedone"))
					transition(edgeName="t011",targetState="moveFailed",cond=whenReply("movefailed"))
					transition(edgeName="t012",targetState="restartToIndoor",cond=whenRequest("deposit"))
					transition(edgeName="t013",targetState="sonarInterrupt",cond=whenDispatch("sonarstop"))
				}	 
				state("restartToIndoor") { //this:State
					action { //it:State
						 lastState = "restartToIndoor"  
						discardMessages = true
						CommUtils.outgreen("TT: restarting to indoor")
							val destinations = landmarkConf.getCoordinatesFor("I")
									val messages = utils.PosUtils.listOfDestToMessStrings(destinations)
									val Xs = messages.first
									val Ys = messages.second
						request("moveclosest", "moveclosest($Xs,$Ys)" ,"trolleyexecutor" )  
						updateResourceRep( "ttstate(moving,indoor)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t014",targetState="takeCharge",cond=whenReply("movecdone"))
					transition(edgeName="t015",targetState="moveFailed",cond=whenReply("movecfailed"))
					transition(edgeName="t016",targetState="sonarInterrupt",cond=whenDispatch("sonarstop"))
				}	 
				state("moveToIndoorFromHome") { //this:State
					action { //it:State
						 lastState = "moveToIndoorFromHome"  
						if( checkMsgContent( Term.createTerm("deposit(FW)"), Term.createTerm("deposit(FW)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
									CurrentLoad = payloadArg(0)
												
												val X = HomeToIndoorCoord.first
												val Y = HomeToIndoorCoord.second
								request("move", "move($X,$Y)" ,"trolleyexecutor" )  
								CommUtils.outgreen("TT: moving robot to indoor")
						}
						updateResourceRep( "ttstate(moving,indoor)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t017",targetState="takeCharge",cond=whenReply("movedone"))
					transition(edgeName="t018",targetState="chargeTakeFailed",cond=whenReply("movefailed"))
					transition(edgeName="t019",targetState="sonarInterrupt",cond=whenDispatch("sonarstop"))
				}	 
				state("takeCharge") { //this:State
					action { //it:State
						CommUtils.outgreen("TT: loading charge")
						delay(1000) 
						CommUtils.outgreen("TT: charge loaded")
						updateResourceRep( "ttstate(stopped,indoor)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="chargeTaken", cond=doswitch() )
				}	 
				state("chargeTaken") { //this:State
					action { //it:State
						answer("deposit", "chargetakentt", "chargetakentt($CurrentLoad)"   )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t020",targetState="toPort",cond=whenRequest("depositstatus"))
				}	 
				state("toPort") { //this:State
					action { //it:State
						 lastState = "toPort"  
							val X = IndoorToPortCoord.first
									val Y = IndoorToPortCoord.second
						CommUtils.outgreen("TT: moving to access port")
						request("move", "move($X,$Y)" ,"trolleyexecutor" )  
						updateResourceRep( "ttstate(moving,port)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t021",targetState="depositInColdRoom",cond=whenReply("movedone"))
					transition(edgeName="t022",targetState="moveFailed",cond=whenReply("movefailed"))
					transition(edgeName="t023",targetState="sonarInterrupt",cond=whenDispatch("sonarstop"))
				}	 
				state("depositInColdRoom") { //this:State
					action { //it:State
						discardMessages = true
						delay(1000) 
						answer("depositstatus", "chargedeposited", "chargedeposited($CurrentLoad)"   )  
						CommUtils.outgreen("TT: charge deposited")
						updateResourceRep( "ttstate(stopped,coldroom)"  
						)
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
				 	 		stateTimer = TimerActor("timer_depositInColdRoom", 
				 	 					  scope, context!!, "local_tout_transporttrolley_depositInColdRoom", 1000.toLong() )
					}	 	 
					 transition(edgeName="t024",targetState="returnHome",cond=whenTimeout("local_tout_transporttrolley_depositInColdRoom"))   
					transition(edgeName="t025",targetState="restartToIndoor",cond=whenRequest("deposit"))
					transition(edgeName="t026",targetState="sonarInterrupt",cond=whenDispatch("sonarstop"))
				}	 
			}
		}
} 
