package hu.agnos.report.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

class KpiTest {

    @Test
    public void testConstructor() {
        List<KpiLabels> labels = new ArrayList<>();
        labels.add(new KpiLabels("en"));
        labels.add(new KpiLabels("fr"));

        Kpi kpi = new Kpi(labels);

        assertFalse(kpi.isRatio());
        assertEquals(2, kpi.getLabels().size());
        assertEquals("en", kpi.getLabels().get(0).getLang());
        assertEquals("fr", kpi.getLabels().get(1).getLang());
    }

    @Test
    public void testAddLanguage() {
        Kpi kpi = new Kpi(new ArrayList<>());
        kpi.addLanguage("es");

        assertEquals(1, kpi.getLabels().size());
        assertEquals("es", kpi.getLabels().get(0).getLang());
    }

    @Test
    public void testRemoveLanguage() {
        List<KpiLabels> labels = new ArrayList<>();
        labels.add(new KpiLabels("en"));
        labels.add(new KpiLabels("fr"));

        Kpi kpi = new Kpi(labels);
        kpi.removeLanguage(0);

        assertEquals(1, kpi.getLabels().size());
        assertEquals("fr", kpi.getLabels().get(0).getLang());
    }

    @Test
    public void testGetParsedBaseLevel() {
        Kpi kpi = new Kpi(new ArrayList<>());
        kpi.setBaseLevel("[ ['level1', 'level2'], ['level3', 'level4'] ]");

        ArrayList<ArrayList<String>> parsedBaseLevel = kpi.getParsedBaseLevel();

        assertNotNull(parsedBaseLevel);
        assertEquals(2, parsedBaseLevel.size());
        assertEquals(2, parsedBaseLevel.get(0).size());
        assertEquals("level1", parsedBaseLevel.get(0).get(0));
        assertEquals("level2", parsedBaseLevel.get(0).get(1));
        assertEquals("level3", parsedBaseLevel.get(1).get(0));
        assertEquals("level4", parsedBaseLevel.get(1).get(1));
    }

    @Test
    void getParsedBaseLevel_withValidBaseLevel_returnsCorrectArray() {
        Kpi kpi = new Kpi();
        kpi.setBaseLevel("[ ['HUN'], ['desi_dps', 'desi_dps_egov', 'desi_dps_pscit'] ]");
        ArrayList<ArrayList<String>> result = kpi.getParsedBaseLevel();
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals("HUN", result.get(0).get(0));
        assertEquals(3, result.get(1).size());
        assertEquals("desi_dps", result.get(1).get(0));
        assertEquals("desi_dps_egov", result.get(1).get(1));
        assertEquals("desi_dps_pscit", result.get(1).get(2));
    }

    @Test
    void getParsedBaseLevel_withEmptyBaseLevel_returnsNull() {
        Kpi kpi = new Kpi();
        kpi.setBaseLevel("");
        ArrayList<ArrayList<String>> result = kpi.getParsedBaseLevel();
        assertNull(result);
    }

    @Test
    void getParsedBaseLevel_withNullBaseLevel_returnsNull() {
        Kpi kpi = new Kpi();
        kpi.setBaseLevel(null);
        ArrayList<ArrayList<String>> result = kpi.getParsedBaseLevel();
        assertNull(result);
    }

    @Test
    void getParsedBaseLevel_withSingleElement_returnsSingleElementArray() {
        Kpi kpi = new Kpi();
        kpi.setBaseLevel("[ ['HUN'] ]");
        ArrayList<ArrayList<String>> result = kpi.getParsedBaseLevel();
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals("HUN", result.get(0).get(0));
    }

    @Test
    void getParsedBaseLevel_withMultipleEmptyElements_returnsEmptySubArrays() {
        Kpi kpi = new Kpi();
        kpi.setBaseLevel("[ [], [], [] ]");
        ArrayList<ArrayList<String>> result = kpi.getParsedBaseLevel();
        assertEquals(3, result.size());
        for (ArrayList<String> subArray : result) {
            assertTrue(subArray.isEmpty());
        }
    }

    @Test
    void getParsedBaseLevel_withMixedEmptyAndNonEmptyElements_returnsCorrectArray() {
        Kpi kpi = new Kpi();
        kpi.setBaseLevel("[ ['HUN'], [], ['desi_dps'] ]");
        ArrayList<ArrayList<String>> result = kpi.getParsedBaseLevel();
        assertEquals(3, result.size());
        assertEquals(1, result.get(0).size());
        assertEquals("HUN", result.get(0).get(0));
        assertTrue(result.get(1).isEmpty());
        assertEquals(1, result.get(2).size());
        assertEquals("desi_dps", result.get(2).get(0));
    }

}