import javax.jcr.query.Query

if (request.getParameter('q')) {
    def jcr = context.getAttribute('jcr')
    /*
    def searchQuery = new QueryBuilder(jcr.workspace.queryManager, jcr.valueFactory).with {
        query(
            source: selector(nodeType: 'nt:unstructured', name: 'items'),
            constraint: and(
                constraint1: descendantNode(selectorName: 'items', path: '/mn:content'),
                constraint2: fullTextSearch(selectorName: 'items', propertyName: 'mn:text', searchTerms: request.getParameter('q'))
            ),
            columns: [
                jcr.workspace.queryManager.QOMFactory.column('items', 'mn:title', 'title'),
                jcr.workspace.queryManager.QOMFactory.column('items', 'excerpt(.)', 'rep:excerpt(.)'),
                jcr.workspace.queryManager.QOMFactory.column('items', 'mn:text', 'text'),
            ] as Column[]
        )
    }
    */
    def searchQuery = jcr.workspace.queryManager.createQuery("//*[jcr:contains(., '${request.getParameter('q')}')]/(@mn:text|rep:excerpt(.))", Query.XPATH)
    request.setAttribute('query', searchQuery)
    
    /*
    def attachSearchQuery = new QueryBuilder(jcr.workspace.queryManager, jcr.valueFactory).with {
        query(
                source: selector(nodeType: 'nt:unstructured', name: 'items'),
                constraint: and(
                        constraint1: descendantNode(selectorName: 'items', path: '/mn:attachments'),
                        constraint2: fullTextSearch(selectorName: 'items', propertyName: 'jcr:content', searchTerms: request.getParameter('q'))
                        )
                )
    }
    */
//    def attachSearchQuery = jcr.workspace.queryManager.createQuery("//*[jcr:contains(., '${request.getParameter('q')}')]/(@jcr:data|rep:excerpt(.))", Query.XPATH)
//    def attachSearchQuery = jcr.workspace.queryManager.createQuery("//element(*, nt:resource)[jcr:contains(jcr:data, '*${request.getParameter('q')}*')]", Query.XPATH)
//    def attachSearchQuery = jcr.workspace.queryManager.createQuery("SELECT * FROM [nt:file] as attachments WHERE CONTAINS(attachments.*,'${request.getParameter('q')}')", Query.JCR_SQL2)
    def attachSearchQuery = jcr.workspace.queryManager.createQuery("//element(*,nt:file)[jcr:contains(jcr:content,'%${request.getParameter('q')}%')]", Query.XPATH)
    request.setAttribute('attachQuery', attachSearchQuery)
}

forward("search.html")
