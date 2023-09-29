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
     with Cluster('ctx_status', graph_attr=nodeattr):
          servicestatusgui=Custom('servicestatusgui','./qakicons/symActorSmall.png')
     with Cluster('ctx_raspberry', graph_attr=nodeattr):
          alarmdevice=Custom('alarmdevice','./qakicons/symActorSmall.png')
          warningdevice=Custom('warningdevice','./qakicons/symActorSmall.png')
     with Cluster('ctx_basicrobot', graph_attr=nodeattr):
          basicrobot=Custom('basicrobot(ext)','./qakicons/externalQActor.png')
     serviceaccessgui >> Edge(color='magenta', style='solid', decorate='true', label='<storerequest<font color="darkgreen"> loadaccepted loadrejected</font> &nbsp; deposit<font color="darkgreen"> ticketaccepted ticketrejected</font> &nbsp; chargestatus<font color="darkgreen"> chargetaken</font> &nbsp; >',  fontcolor='magenta') >> coldstorageservice
diag
