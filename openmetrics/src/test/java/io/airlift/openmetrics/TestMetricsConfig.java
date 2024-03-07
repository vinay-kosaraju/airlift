/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.airlift.openmetrics;

import com.google.common.collect.ImmutableMap;
import io.airlift.configuration.testing.ConfigAssertions;
import org.testng.annotations.Test;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;

public class TestMetricsConfig
{
    @Test
    public void testDefaults()
    {
        ConfigAssertions.assertRecordedDefaults(ConfigAssertions.recordDefaults(MetricsConfig.class)
                .setJmxObjectNames(""));
    }

    @Test
    public void testExplicitPropertyMappings()
    {
        Map<String, String> properties = new ImmutableMap.Builder<String, String>()
                .put("openmetrics.jmx-object-names", "foo.bar:name=baz,type=qux|baz.bar:*")
                .build();

        MetricsConfig expected = new MetricsConfig()
                .setJmxObjectNames("foo.bar:name=baz,type=qux|baz.bar:*");

        ConfigAssertions.assertFullMapping(properties, expected);
    }

    @Test
    public void testJmxObjectNames()
            throws MalformedObjectNameException
    {
        MetricsConfig metricsConfig = new MetricsConfig()
                .setJmxObjectNames("foo.bar:name=baz,type=qux|baz.bar:*");

        assertEquals(metricsConfig.getJmxObjectNames(), List.of(new ObjectName("foo.bar:name=baz,type=qux"), new ObjectName("baz.bar:*")));
    }
}
