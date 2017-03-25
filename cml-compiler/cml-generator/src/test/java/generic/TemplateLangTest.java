package generic;

import cml.language.features.Concept;
import cml.language.foundation.Property;
import com.google.common.io.Resources;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.stringtemplate.v4.ST;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableCollection;
import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertNotNull;

@RunWith(Parameterized.class)
public abstract class TemplateLangTest extends TemplateTest
{
    private static final Charset OUTPUT_FILE_ENCODING = Charset.forName("UTF-8");
    private static final String EXPECTED_OUTPUT_PATH = "/%s/%s/%s";
    private static final String LANG_GROUP_PATH = "lang/%s";

    protected static final Collection<String> commonNames = unmodifiableCollection(asList("SomeConcept", "someVar"));

    @SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
    @Parameters
    public static Collection<String> targetLanguages()
    {
        return asList("java");
    }

    private final String targetLanguage;

    protected TemplateLangTest(String targetLanguage)
    {
        this.targetLanguage = targetLanguage;
    }

    @Override
    public void setUp()
    {
        super.setUp();

        groupFile.importTemplates(createTemplateGroupFile(format(LANG_GROUP_PATH, targetLanguage)));
    }

    protected void testTemplateWithConcept(String templateName, Concept concept, String expectedOutputPath)
        throws IOException
    {
        final ST template = getTemplate(templateName);
        assertNotNull("Expected template: " + templateName, template);

        template.add("concept", concept);

        final String result = template.render();
        assertThatOutputMatches(expectedOutputPath, result);
    }

    protected void testTemplateWithProperty(String templateName, Property property, String expectedOutputPath)
        throws IOException
    {
        final ST template = getTemplate(templateName);

        template.add("property", property);

        final String result = template.render();

        assertThatOutputMatches(expectedOutputPath, result);
    }

    protected void assertThatOutputMatches(String expectedOutputPath, String actualOutput) throws IOException
    {
        expectedOutputPath = format(EXPECTED_OUTPUT_PATH, targetLanguage, getTemplatePath(), expectedOutputPath);

        final URL expectedOutputResource = getClass().getResource(expectedOutputPath);
        assertNotNull("Expected output resource must exist: " + expectedOutputPath, expectedOutputResource);

        final String expectedOutput = Resources.toString(expectedOutputResource, OUTPUT_FILE_ENCODING);
        assertEquals(expectedOutputPath, expectedOutput, actualOutput);
    }
}