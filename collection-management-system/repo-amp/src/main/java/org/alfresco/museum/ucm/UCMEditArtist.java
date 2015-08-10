package org.alfresco.museum.ucm;

import static org.alfresco.museum.ucm.UCMConstants.TYPE_UCM_ARTIST_ARTIFACT_QNAME;
import static org.alfresco.museum.ucm.UCMConstants.TYPE_UCM_ARTIST_QNAME;

import org.alfresco.repo.forms.FormData;
import org.alfresco.repo.forms.processor.node.UCMGenericFilter;
import org.alfresco.service.cmr.dictionary.TypeDefinition;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.namespace.QName;
import org.springframework.util.StringUtils;

/**
 * Handle artist update operation. Image selected by user should update artist artifact object instead of artist itself.
 */
public class UCMEditArtist extends UCMGenericFilter<NodeRef> {
	/**
	 * Store "cm:content" property value, which is ignored by default handler.<br/>
	 */
	@Override
	public void afterPersist(NodeRef item, FormData data, NodeRef persistedObject) {
		QName nodeType = this.getNodeService().getType(persistedObject);
		boolean isArtist = nodeType.equals(TYPE_UCM_ARTIST_QNAME);
		if (isArtist) {
			Object artistArtifactValue = this.getNodeService().getProperty(persistedObject, UCMConstants.PROP_UCM_ARTIST_ARTIFACT_QNAME);
			if (!StringUtils.isEmpty(artistArtifactValue)) {
				String artistArtifactString = artistArtifactValue.toString();
				NodeRef artistArtifactRef = new NodeRef(artistArtifactString);
				TypeDefinition artistArtifactType = this.getDictionaryService().getType(
						UCMConstants.TYPE_UCM_ARTIST_ARTIFACT_QNAME);
				writeContent(artistArtifactType, data, artistArtifactRef);
			}
		}
		else {
			boolean isArtistArtifact = nodeType.equals(TYPE_UCM_ARTIST_ARTIFACT_QNAME);
			if (isArtistArtifact) {
				TypeDefinition artistArtifactType = this.getDictionaryService().getType(
						UCMConstants.TYPE_UCM_ARTIST_ARTIFACT_QNAME);
				writeContent(artistArtifactType, data, persistedObject);
			}
		}
	}
}
