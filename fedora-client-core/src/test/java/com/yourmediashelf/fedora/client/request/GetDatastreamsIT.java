/**
 * Copyright (C) 2010 MediaShelf <http://www.yourmediashelf.com/>
 *
 * This file is part of fedora-client.
 *
 * fedora-client is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * fedora-client is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with fedora-client.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.yourmediashelf.fedora.client.request;

import static com.yourmediashelf.fedora.client.FedoraClient.addDatastream;
import static com.yourmediashelf.fedora.client.FedoraClient.getDatastreams;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.yourmediashelf.fedora.client.FedoraClientException;
import com.yourmediashelf.fedora.client.response.GetDatastreamsResponse;
import com.yourmediashelf.fedora.generated.management.DatastreamProfile;

public class GetDatastreamsIT extends BaseFedoraRequestIT {

    @Test
    public void testGetDatastreams() throws Exception {
        String dsId = "testGetDatastreams";
        // first, add an inline datastream
        String content = "<foo>bar</foo>";
        addDatastream(testPid, dsId).content(content).execute();

        // list datastreams
        GetDatastreamsResponse response = getDatastreams(testPid).execute();
        assertEquals(200, response.getStatus());
        assertEquals(testPid, response.getPid());
        // response baseUrl is appending a /
        String baseUrl = getCredentials().getBaseUrl().toString() + "/";
        assertEquals(baseUrl, response.getBaseUrl());
        assertNull(response.getAsOfDateTime());

        List<DatastreamProfile> datastreams = response.getDatastreamProfiles();
        boolean found = false;
        for (DatastreamProfile d : datastreams) {
            if (d.getDsID().equals(dsId)) {
                found = true;
            }
        }
        assertTrue("Expected to find dsId among the list of datastreams", found);
    }

    @Override
    public void testNoDefaultClientRequest() throws FedoraClientException {
        testNoDefaultClientRequest(getDatastreams(testPid), 200);
    }
}
