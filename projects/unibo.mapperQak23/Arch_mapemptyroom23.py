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
with Diagram('mapemptyroom23Arch', show=False, outformat='png', graph_attr=graphattr) as diag:
  with Cluster('env'):
     sys = Custom('','./qakicons/system.png')
### see https://renenyffenegger.ch/notes/tools/Graphviz/attributes/label/HTML-like/index
     with Cluster('ctxbasicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     with Cluster('ctxmapemptyroom23', graph_attr=nodeattr):
          mapemptyroom23=Custom('mapemptyroom23','./qakicons/symActorSmall.png')
     mapemptyroom23 >> Edge(color='magenta', style='solid', decorate='true', label='<engage &nbsp; step &nbsp; >',  fontcolor='magenta') >> basicrobot
     mapemptyroom23 >> Edge(color='blue', style='solid',  label='<cmd &nbsp; disengage &nbsp; >',  fontcolor='blue') >> basicrobot
diag
