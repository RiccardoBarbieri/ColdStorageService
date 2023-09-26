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
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctx_coldstorageservice', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
          trolleyexecutor=Custom('trolleyexecutor','./qakicons/symActorSmall.png')
     coldstorageservice >> Edge(color='blue', style='solid', xlabel='deposit', fontcolor='blue') >> transporttrolley
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='getposition', fontcolor='magenta') >> trolleyexecutor
     transporttrolley >> Edge(color='magenta', style='solid', xlabel='move', fontcolor='magenta') >> trolleyexecutor
     transporttrolley >> Edge(color='blue', style='solid', xlabel='chargetakentt', fontcolor='blue') >> coldstorageservice
     transporttrolley >> Edge(color='blue', style='solid', xlabel='chargedeposited', fontcolor='blue') >> coldstorageservice
     trolleyexecutor >> Edge(color='magenta', style='solid', xlabel='engage', fontcolor='magenta') >> basicrobot
     trolleyexecutor >> Edge(color='blue', style='solid', xlabel='fail', fontcolor='blue') >> transporttrolley
     trolleyexecutor >> Edge(color='blue', style='solid', xlabel='setrobotstate', fontcolor='blue') >> basicrobot
     trolleyexecutor >> Edge(color='magenta', style='solid', xlabel='moverobot', fontcolor='magenta') >> basicrobot
     trolleyexecutor >> Edge( xlabel='alarm', **eventedgeattr, fontcolor='red') >> sys
     trolleyexecutor >> Edge(color='magenta', style='solid', xlabel='getrobotstate', fontcolor='magenta') >> basicrobot
diag
