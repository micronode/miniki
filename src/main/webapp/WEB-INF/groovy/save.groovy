import org.apache.jackrabbit.util.Text;

if (request.getParameter('p')) {
    def node = context.jcr.getNode(request.getParameter('p'))
//    node.checkout()
    
    def properties = request.parameterMap.entrySet().findAll { it.key ==~ /^mn:.*/}
    node.session.save {
//        node['title'] = Text.escapeIllegalJcrChars(request.getParameter('t'))
//        node['text'] = request.getParameter('txt')
        properties.each {
            if (it.value.length == 1) {
                node[it.key] = it.value[0]
            }
            else {
                node[it.key] = it.value
            }
        }
    }
//    node.checkin()
    redirect "view${node.path - /mn:content\//}/"
}
forward 'view.html'