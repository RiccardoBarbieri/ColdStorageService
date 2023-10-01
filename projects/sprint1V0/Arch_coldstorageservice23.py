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
     with Cluster('ctx_coldstorageservice', graph_attr=nodeattr):
          coldstorageservice=Custom('coldstorageservice','./qakicons/symActorSmall.png')
          transporttrolley=Custom('transporttrolley','./qakicons/symActorSmall.png')
     with Cluster('ctx_access', graph_attr=nodeattr):
          serviceaccessgui=Custom('serviceaccessgui','./qakicons/symActorSmall.png')
     with Cluster('ctx_statusmonitor', graph_attr=nodeattr):
          servicestatusgui=Custom('servicestatusgui','./qakicons/symActorSmall.png')
     with Cluster('ctx_alarmsystem', graph_attr=nodeattr):
          warningdevice=Custom('warningdevice','./qakicons/symActorSmall.png')
          alarmdevice=Custom('alarmdevice','./qakicons/symActorSmall.png')
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          basicrobotsim=Custom('basicrobotsim','./qakicons/symActorSmall.png')
     coldstorageservice >> Edge( label='rejrequpdate', **eventedgeattr, fontcolor='red') >> sys
     coldstorageservice >> Edge( label='coldroomupdate', **eventedgeattr, fontcolor='red') >> sys
     transporttrolley >> Edge( label='statusupdate', **eventedgeattr, fontcolor='red') >> sys
     serviceaccessgui >> Edge(color='magenta', style='solid', decorate='true', label='<storerequest &nbsp; codeentered &nbsp; >',  fontcolor='magenta') >> coldstorageservice
     coldstorageservice >> Edge(color='blue', style='solid',  label='<newcharge &nbsp; >',  fontcolor='blue') >> transporttrolley
     transporttrolley >> Edge(color='blue', style='solid',  label='<goToINDOOR &nbsp; goToColdRoom &nbsp; >',  fontcolor='blue') >> basicrobotsim
     basicrobotsim >> Edge(color='blue', style='solid',  label='<arrivedINDOOR &nbsp; arrivedColdRoom &nbsp; arrivedHOME &nbsp; >',  fontcolor='blue') >> transporttrolley
     transporttrolley >> Edge(color='blue', style='solid',  label='<chargedeposited &nbsp; >',  fontcolor='blue') >> coldstorageservice
     alarmdevice >> Edge(color='blue', style='solid',  label='<stop &nbsp; resume &nbsp; >',  fontcolor='blue') >> transporttrolley
     coldstorageservice >> Edge(color='blue', style='solid',  label='<chargetaken &nbsp; >',  fontcolor='blue') >> serviceaccessgui
diag
