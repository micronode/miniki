import java.security.MessageDigest;

import javax.jcr.Value;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload

if (request.method != 'POST') {
    request.getRequestDispatcher("view.html").forward(request, response)
}

if (ServletFileUpload.isMultipartContent(request)) {
    // Create a new file upload handler
    ServletFileUpload upload = [new DiskFileItemFactory()]

    def node
    def items = upload.getItemIterator(request)
    while (items.hasNext()) {
        def item = items.next()
        if (item.formField && item.fieldName == 'p') {
            node = context.getAttribute('jcr').getNode(item.openStream().text)
        }
        else {
            def digest = MessageDigest.getInstance('md5')
            def binary = node.session.valueFactory.createBinary(item.openStream())
//            item.openStream().with {
            binary.stream.with {
                byte[] data = new byte[8192]
                int bytesRead
                while ((bytesRead = read(data)) >= 0) {
                    digest.update data, 0, bytesRead
                }
            }
            def checksum = new String(Hex.encodeHex(digest.digest()))
            
            def attachNode = node.session.rootNode << 'attachments'
            checksum.split(/(?<=\G.{2})/).each {
                attachNode = attachNode << it
            }
            if (attachNode.isNew()) {
//                println "Creating node: $attachNode.path"
                attachNode.addMixin('mix:referenceable')
                attachNode.session.save {
//                    attachNode['jcr:content'] = attachNode.session.valueFactory.createBinary(item.openStream())
                    attachNode['jcr:content'] = binary
                }
            }
            
            if (node) {
                node.session.save {
                    def attachments = node << 'attachments'
                    attachments[item.name] = attachNode
                }
//                    attachments = node.attachments.values as List
//                }
//                else {
//                    attachments = []
//                }
//                attachments << node.session.valueFactory.createValue(attachNode)
//                
//                node.session.save {
//                    node['attachments'] = attachments as Value[]
//                }
            }
        }
    }
    response.sendRedirect("view.html?p=$node.path")
}
