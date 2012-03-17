import org.mnode.juicer.query.QueryBuilder

if (request.getParameter('q')) {
    def jcr = context.getAttribute('jcr')
    def searchQuery = new QueryBuilder(jcr.workspace.queryManager, jcr.valueFactory).with {
        query(
            source: selector(nodeType: 'nt:unstructured', name: 'items'),
            constraint: and(
                constraint1: descendantNode(selectorName: 'items', path: '/content'),
                constraint2: fullTextSearch(selectorName: 'items', propertyName: 'text', searchTerms: request.getParameter('q'))
            )
        )
    }
    request.setAttribute('query', searchQuery)
}

request.getRequestDispatcher("search.html").forward(request, response)
