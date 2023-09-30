/* Generated by AN DISI Unibo */ 
package it.unibo.serviceaccessgui

import it.unibo.kactor.*
import alice.tuprolog.*
import unibo.basicomm23.*
import unibo.basicomm23.interfaces.*
import unibo.basicomm23.utils.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
	
class Serviceaccessgui ( name: String, scope: CoroutineScope  ) : ActorBasicFsm( name, scope ){

	override fun getInitialState() : String{
		return "s0"
	}
	override fun getBody() : (ActorBasicFsm.() -> Unit){
		val interruptedStateTransitions = mutableListOf<Transition>()
			var TicketList = mutableListOf<String>() 
				var CurTicket: String = ""
				var IsRequestRejected = false
		return { //this:ActionBasciFsm
				state("s0") { //this:State
					action { //it:State
						delay(3000) 
						request("storerequest", "storerequest(30)" ,"coldstorageservice" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("work") { //this:State
					action { //it:State
						CommUtils.outgreen("$name in ${currentState.stateName} | $currentMsg | ${Thread.currentThread().getName()} n=${Thread.activeCount()}")
						 	   
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition(edgeName="t017",targetState="handleStorereply",cond=whenReply("storereply"))
					transition(edgeName="t018",targetState="handleTicketreply",cond=whenReply("ticketreply"))
					transition(edgeName="t019",targetState="handleChargetaken",cond=whenDispatch("chargetaken"))
				}	 
				state("handleStorereply") { //this:State
					action { //it:State
						 IsRequestRejected = false  
						if( checkMsgContent( Term.createTerm("storereply(TICKET,OKREJ)"), Term.createTerm("storereply(TICKET,OKREJ)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  payloadArg(2) == "REJ"  
								 ){CommUtils.outred("SAG | Store request rejected, not enough space")
								 IsRequestRejected = true  
								}
								else
								 {CommUtils.outblue("SAG | Store request accepted, your ticket number is ${payloadArg(1)}")
								  TicketList.add(payloadArg(1))  
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitchGuarded({ IsRequestRejected  
					}) )
					transition( edgeName="goto",targetState="insertCode", cond=doswitchGuarded({! ( IsRequestRejected  
					) }) )
				}	 
				state("insertCode") { //this:State
					action { //it:State
						delay(2000) 
						 CurTicket = TicketList.get(0)  
						request("codeentered", "codeentered($CurTicket)" ,"coldstorageservice" )  
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleTicketreply") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("ticketreply(TICKET,OKREJ)"), Term.createTerm("ticketreply(TICKET,OKNO)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								if(  payloadArg(2) == "REJ"  
								 ){CommUtils.outred("SAG | You did not reach INDOOR in time, the request is forfeit")
								}
								else
								 {CommUtils.outblue("SAG | Ticket accepted, wait here for cargo unload")
								  TicketList.remove(payloadArg(1))  
								 }
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="work", cond=doswitch() )
				}	 
				state("handleChargetaken") { //this:State
					action { //it:State
						if( checkMsgContent( Term.createTerm("chargetaken(TICKET)"), Term.createTerm("chargetaken(TICKET)"), 
						                        currentMsg.msgContent()) ) { //set msgArgList
								CommUtils.outblack("SAG | Ticket ${payloadArg(1)} driver can move away from INDOOR")
						}
						//genTimer( actor, state )
					}
					//After Lenzi Aug2002
					sysaction { //it:State
					}	 	 
					 transition( edgeName="goto",targetState="s0", cond=doswitch() )
				}	 
			}
		}
}
