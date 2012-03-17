import org.apache.jackrabbit.util.Text;

if (request.getParameter('p')) {
    def node = context.getAttribute('jcr').getNode(request.getParameter('p'))
    node.session.save {
        node['title'] = Text.escapeIllegalJcrChars(request.getParameter('t'))
        node['text'] = request.getParameter('txt')
    }
}

request.getRequestDispatcher("view.html").forward(request, response)
