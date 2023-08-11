/*
 *  Crea una mappa di una stanza vuota
 */
System mapemptyroom23 //-msglog  
  
Dispatch cmd       : cmd(MOVE)  
Request step       : step(TIME)	  
Reply stepdone     : stepdone(V)  
Reply stepfailed   : stepfailed(DURATION, CAUSE)
Event alarm        : alarm(X)

Request engage        : engage(OWNER, STEPTIME)	
Reply   engagedone    : engagedone(ARG)
Reply   engagerefused : engagerefused(ARG)

Dispatch disengage    : disengage(ARG)
//Dispatch engaged      : engaged(ARG)


Context ctxbasicrobot      ip [host="127.0.0.1" port=8020]   
Context ctxmapemptyroom23  ip [host="localhost" port=8032]

ExternalQActor basicrobot context ctxbasicrobot  

/*   
 * ------------------------------------------------------------------
 *  cleanerwalker 
 * ------------------------------------------------------------------
*/     
QActor mapemptyroom23 context ctxmapemptyroom23 {
	[# var StepTime  = 300L
	   val planner   = unibo.planner23.Planner23Util()
	   
	   val HEIGHT = 10
	   val WIDTH = 10
	   
	   var NumTurn = 0
	   
	   var Visited: MutableSet<Pair<Int,Int>> = mutableSetOf()
	   val MapName   = "mapBFS23"
	   val PosQueue  = ArrayDeque<Pair<Int,Int>>(listOf(Pair<Int,Int>(0,0)))
	   var OldPos    = Pair<Int,Int>(0,0)
	   var CurPos    = Pair<Int,Int>(0,0)
	   val Obstacles = ArrayList<Pair<Int,Int>>()
	   var moves: ArrayList<String> = arrayListOf()
	#]
	
	State s0 initial {
		println("mapperbuilder starts") color blue
		[# planner.initAI()  #]
		[# planner.showMap() #]
		request basicrobot -m engage : engage(mapemptyroom23,$StepTime)
	}
	Transition t0 whenReply engagedone -> checkQueue
	
	State checkQueue {
		
	}
	Goto updatePos if [# !PosQueue.isEmpty() #] else endWork
	
	State updatePos {
		[#	OldPos = CurPos
			CurPos = PosQueue.removeFirst()
			Visited.add(CurPos)
		#]
	}
	Goto seekStart
	
	State seekStart {
		println("SEEKING to $CurPos") color green
		[#	moves = utils.computeMoves(OldPos, CurPos) #]
		println("MOVES = $moves") color green
	}
	Goto seekLoop if [#	!moves.isEmpty() #] else expandCheckObstacle
	
	State seekLoop {
		[#	val Move = moves.removeAt(0) #]
		
		if [#	(Move == "l") or (Move == "r") #] {
			forward basicrobot -m cmd : cmd($Move)
		}
		else {
			request basicrobot -m step : step($StepTime)
		}
		delay 400
	}
	Goto seekLoop if [#	!moves.isEmpty() #] else expandCheckObstacle //endWork
	
	State expandCheckObstacle {
		println("END SEEKING to $CurPos") color green
		[#	NumTurn = 0 #]
	}
	Goto goAheadExplore
	
	State goAheadExplore {
		println("EXPLORING $CurPos") color red
		request basicrobot -m step : step($StepTime)
	}
	Transition t0	whenReply stepdone -> backtrack
					whenReply stepfailed -> registerObstacle
	
	State turnExplore {
		println("TURNING number $NumTurn") color red
		if [#	NumTurn < 4 #] {
			forward basicrobot -m cmd : cmd(l)
			[#	NumTurn += 1 #]
		}
		if [#	NumTurn == 4 #] {
			[#	PosQueue.addAll(utils.updatePosQueue(CurPos, Obstacles, Visited)) #]
		}
	}
	Goto checkQueue if [#	NumTurn == 4 #] else goAheadExplore
					
	State registerObstacle{
		println("REGISTER OBSTACLE at ") color red
		[#	val tempPos = utils.getTempPos(CurPos, NumTurn)
			Obstacles.add(tempPos)
			CommUtils.outred("REGISTER OBSTACLE at $tempPos")
		#]
	}
	Goto turnExplore
	
	State backtrack {
		println("BACKTRACKING to $CurPos") color red
		forward basicrobot -m cmd : cmd(l)
		forward basicrobot -m cmd : cmd(l)
		request basicrobot -m step : step($StepTime)
		forward basicrobot -m cmd : cmd(l)
		forward basicrobot -m cmd : cmd(l)
		
	}
	Goto turnExplore
    
    State endWork {
 		[#	CommUtils.outgreen(Obstacles.toString())
 			CommUtils.outgreen(Visited.toString())
		#]
		forward basicrobot -m  disengage : disengage(_)
		println("mapperbuilder BYE") 
 		[# System.exit(0) #]
	}  
	              
}