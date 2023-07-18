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
     coldstorageservice >> Edge( xlabel='rejrequpdate', **eventedgeattr, fontcolor='red') >> sys
     coldstorageservice >> Edge(color='blue', style='solid', xlabel='newcharge', fontcolor='blue') >> transporttrolley
     coldstorageservice >> Edge(color='blue', style='solid', xlabel='chargetaken', fontcolor='blue') >> serviceaccessgui
     coldstorageservice >> Edge( xlabel='coldroomupdate', **eventedgeattr, fontcolor='red') >> sys
     basicrobotsim >> Edge(color='blue', style='solid', xlabel='arrivedINDOOR', fontcolor='blue') >> transporttrolley
     basicrobotsim >> Edge(color='blue', style='solid', xlabel='arrivedColdRoom', fontcolor='blue') >> transporttrolley
     basicrobotsim >> Edge(color='blue', style='solid', xlabel='arrivedHOME', fontcolor='blue') >> transporttrolley
     transporttrolley >> Edge( xlabel='statusupdate', **eventedgeattr, fontcolor='red') >> sys
     transporttrolley >> Edge(color='blue', style='solid', xlabel='goToINDOOR', fontcolor='blue') >> basicrobotsim
     transporttrolley >> Edge(color='blue', style='solid', xlabel='goToColdRoom', fontcolor='blue') >> basicrobotsim
     transporttrolley >> Edge(color='blue', style='solid', xlabel='chargedeposited', fontcolor='blue') >> coldstorageservice
     serviceaccessgui >> Edge(color='magenta', style='solid', xlabel='storerequest', fontcolor='magenta') >> coldstorageservice
     serviceaccessgui >> Edge(color='magenta', style='solid', xlabel='codeentered', fontcolor='magenta') >> coldstorageservice
     sys >> Edge(color='red', style='dashed', xlabel='coldroomupdate', fontcolor='red') >> servicestatusgui
     sys >> Edge(color='red', style='dashed', xlabel='rejrequpdate', fontcolor='red') >> servicestatusgui
     sys >> Edge(color='red', style='dashed', xlabel='statusupdate', fontcolor='red') >> servicestatusgui
     sys >> Edge(color='red', style='dashed', xlabel='statusupdate', fontcolor='red') >> warningdevice
     alarmdevice >> Edge(color='blue', style='solid', xlabel='stop', fontcolor='blue') >> transporttrolley
     alarmdevice >> Edge(color='blue', style='solid', xlabel='resume', fontcolor='blue') >> transporttrolley
diag
