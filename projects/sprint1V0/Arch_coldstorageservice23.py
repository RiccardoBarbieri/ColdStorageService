### conda install diagrams
from diagrams import Cluster, Diagram, Edge
from diagrams.custom import Custom
import os
os.environ['PATH'] += os.pathsep + 'C:/Program Files/Graphviz/bin/'

graphattr = {     #https://www.graphviz.org/doc/info/attrs.html
    'fontsize': '22',
}

nodeattr = {   
    'fontsize': '22',
    'bgcolor': 'lightyellow'
}

eventedgeattr = {
    'color': 'red',
    'style': 'dotted'
}
with Diagram('coldstorageservice23Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_coldstorageservice', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          trolleyexecutor=Custom('trolleyexecutor','./qakicons/symActorSmall.png')
     trolleyexecutor >> Edge( label='alarm', **eventedgeattr, fontcolor='red') >> sys
     coldstorageservice >> Edge(color='magenta', style='solid', decorate='true', label='<deposit<font color="darkgreen"> chargetakentt chargefailedtt</font> &nbsp; depositstatus<font color="darkgreen"> chargedeposited chargedepfailed</font> &nbsp; >',  fontcolor='magenta') >> transporttrolley
     transporttrolley >> Edge(color='magenta', style='solid', decorate='true', label='<move<font color="darkgreen"> movedone movefailed</font> &nbsp; moveclosest<font color="darkgreen"> movecdone movecfailed</font> &nbsp; >',  fontcolor='magenta') >> trolleyexecutor
     trolleyexecutor >> Edge(color='magenta', style='solid', decorate='true', label='<engage<font color="darkgreen"> engagedone engagerefused</font> &nbsp; moverobot<font color="darkgreen"> moverobotdone moverobotfailed</font> &nbsp; getrobotstate<font color="darkgreen"> robotstate</font> &nbsp; >',  fontcolor='magenta') >> basicrobot
     trolleyexecutor >> Edge(color='blue', style='solid',  label='<fail &nbsp; >',  fontcolor='blue') >> transporttrolley
     trolleyexecutor >> Edge(color='blue', style='solid',  label='<setrobotstate &nbsp; >',  fontcolor='blue') >> basicrobot
diag
