if (request.getAttribute('mn:attachment')) {
    def node = request.getAttribute('mn:node')
    def attachment = node.getNode('mn:attachments')[request.getAttribute('mn:attachment') - /^\/*/]?.getNode()
    response.contentType = attachment['jcr:mimeType']
    sout << attachment['jcr:content']['jcr:data'].binary.stream
}
else {
    forward 'view.html'
}