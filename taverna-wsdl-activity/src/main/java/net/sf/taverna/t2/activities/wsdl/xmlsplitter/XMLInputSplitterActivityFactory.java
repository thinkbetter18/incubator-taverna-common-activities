/*******************************************************************************
 * Copyright (C) 2010 The University of Manchester
 *
 *  Modifications to the initial code base are copyright of their
 *  respective authors, or their employers as appropriate.
 *
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU Lesser General Public License
 *  as published by the Free Software Foundation; either version 2.1 of
 *  the License, or (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but
 *  WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *  Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public
 *  License along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307
 ******************************************************************************/
package net.sf.taverna.t2.activities.wsdl.xmlsplitter;

import java.io.IOException;
import java.net.URI;
import java.util.HashSet;
import java.util.Set;

import net.sf.taverna.t2.workflowmodel.Edits;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityConfigurationException;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityFactory;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityInputPort;
import net.sf.taverna.t2.workflowmodel.processor.activity.ActivityOutputPort;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * An {@link ActivityFactory} for creating <code>XMLInputSplitterActivity</code>.
 *
 * @author David Withers
 */
public class XMLInputSplitterActivityFactory implements ActivityFactory {

	private Edits edits;

	@Override
	public XMLInputSplitterActivity createActivity() {
		return new XMLInputSplitterActivity();
	}

	@Override
	public URI getActivityType() {
		return URI.create(XMLInputSplitterActivity.URI);
	}

	@Override
	public JsonNode getActivityConfigurationSchema() {
		ObjectMapper objectMapper = new ObjectMapper();
		try {
 			return objectMapper.readTree(getClass().getResource("/xml-splitter.schema.json"));
		} catch (IOException e) {
			return objectMapper.createObjectNode();
		}
	}

	public void setEdits(Edits edits) {
		this.edits = edits;
	}

	@Override
	public Set<ActivityInputPort> getInputPorts(JsonNode configuration)
			throws ActivityConfigurationException {
		Set<ActivityInputPort> inputPorts = new HashSet<>();
		if (configuration.has("inputPorts")) {
			for (JsonNode inputPort : configuration.get("inputPorts")) {
				inputPorts.add(edits.createActivityInputPort(inputPort.get("name").textValue(),
						inputPort.get("depth").intValue(), false, null, String.class));
			}
		}
		return inputPorts;
	}

	@Override
	public Set<ActivityOutputPort> getOutputPorts(JsonNode configuration)
			throws ActivityConfigurationException {
		Set<ActivityOutputPort> outputPorts = new HashSet<>();
		if (configuration.has("outputPorts")) {
			for (JsonNode outputPort : configuration.get("outputPorts")) {
				outputPorts.add(edits.createActivityOutputPort(outputPort.get("name").textValue(),
						outputPort.get("depth").intValue(), outputPort.get("granularDepth").intValue()));
			}
		}
		return outputPorts;
	}

}
