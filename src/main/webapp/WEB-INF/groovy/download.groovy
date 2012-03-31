if (request.getParameter('p')) {
    def node = context.getAttribute('jcr').getNode(request.getParameter('p'))
//    response.contentType = ''
    response.setHeader('Content-Disposition', "attachment;filename=${request.getParameter('f')}")
    sout << node['jcr:content'].binary.stream
//    sout << node['jcr:content'].binary.stream.bytes
}
