/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hu.agnos.report.util;

import hu.agnos.report.entity.Indicator;
import hu.agnos.report.entity.Hierarchy;
import hu.agnos.report.entity.Level;
import hu.agnos.report.entity.Report;
import hu.agnos.report.entity.Visualization;
import java.util.Base64;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 *
 * @author parisek
 */
public class JsonMarshaller {

    public static String getJSONFull(Report report) {
        JSONObject obj = new JSONObject();
        obj.put("cube_unique_name", report.getCubeName() + ":" + report.getName());
        JSONArray jsonLanguages = new JSONArray();
        jsonLanguages.addAll(report.getLanguages());
        obj.put("languages", jsonLanguages);
        JSONArray jsonIndicators = new JSONArray();
        for (Indicator i : report.getIndicators()) {
            jsonIndicators.add(getIndicatorJSON(i));
        }
        obj.put("indicators", jsonIndicators);
        JSONArray jsonDimensions = new JSONArray();
        for (Hierarchy h : report.getHierarchies()) {
            jsonDimensions.add(getHierarchyJSON(h));
        }
        obj.put("dimensions", jsonDimensions);
        JSONArray jsonVisualizations = new JSONArray();
        for (Visualization v : report.getVisualizations()) {
            jsonVisualizations.add(v.toString());
        }
        obj.put("visualization", jsonVisualizations);
        return obj.toJSONString();
    }

    public static String getJSONHeader(Report report) {
        JSONObject obj = new JSONObject();
        obj.put("name", report.getCubeName() + ":" + report.getName());
        JSONArray jsonLanguages = new JSONArray();
        jsonLanguages.addAll(report.getLanguages());
        obj.put("languages", jsonLanguages);
        obj.put("updated", "ValueOfUpdatedIsOnlyRevealedAtRuntime");
        JSONArray jsonCaptions = new JSONArray();
        jsonCaptions.addAll(report.getCaptions());
        obj.put("captions", jsonCaptions);
        JSONArray jsonHelps = new JSONArray();
        for (String helps : report.getHelps()) {
            jsonHelps.add(base64encoder(helps));
        }
        obj.put("helpHTMLs", jsonHelps);
        JSONArray jsonDescriptions = new JSONArray();
        jsonDescriptions.addAll(report.getDescriptions());
        obj.put("descriptions", jsonDescriptions);
        JSONArray jsonDatasources = new JSONArray();
        jsonDatasources.addAll(report.getDatasource());
        obj.put("datasources", jsonDatasources);
        return obj.toJSONString();
    }

    private static JSONObject getHierarchyJSON(Hierarchy hierarchy) {
        JSONObject obj = new JSONObject();

        obj.put("hierarchy_unique_name", hierarchy.getHierarchyUniqueName());

        JSONArray jsonCaptions = new JSONArray();
        jsonCaptions.addAll(hierarchy.getCaptions());
        obj.put("captions", jsonCaptions);

        JSONArray jsonDescriptions = new JSONArray();
        jsonDescriptions.addAll(hierarchy.getDescriptions());
        obj.put("descriptions", jsonDescriptions);

        JSONArray jsonTopLevelCaptions = new JSONArray();
        jsonTopLevelCaptions.addAll(hierarchy.getToplevelStrings());
        obj.put("top_level_captions", jsonTopLevelCaptions);

        obj.put("type", hierarchy.getType());

        JSONArray jsonLevels = new JSONArray();
        for (Level l : hierarchy.getLevels()) {
            if (l.getDepth() <= hierarchy.getAllowedDepth()) {
                jsonLevels.add(getLevelJSON(l));
            }
        }
        obj.put("levels", jsonLevels);

        return obj;
    }

    private static JSONObject getLevelJSON(Level level) {
        JSONObject obj = new JSONObject();
        obj.put("level", level.getDepth());
        obj.put("level_id", level.getIdColumnName());
        obj.put("level_known_id", level.getCodeColumnName());
        obj.put("level_name_hu", level.getNameColumnName());
        return obj;
    }

    private static JSONObject getIndicatorJSON(Indicator indicator) {
        JSONObject obj = new JSONObject();

        JSONArray jsonCaptions = new JSONArray();
        jsonCaptions.addAll(indicator.getCaptions());
        obj.put("captions", jsonCaptions);

        JSONArray jsonDescriptions = new JSONArray();
        jsonDescriptions.addAll(indicator.getDescriptions());
        obj.put("descriptions", jsonDescriptions);

        JSONObject val = new JSONObject();
        val.put("measure_unique_name", indicator.getValue().getMeasureUniqueName());

        JSONArray jsonUnits = new JSONArray();
        jsonUnits.addAll(indicator.getValue().getUnits());
        val.put("units", jsonUnits);

        JSONArray jsonUnitPlurals = new JSONArray();
        jsonUnitPlurals.addAll(indicator.getValue().getUnitPlurals());
        val.put("unitPlurals", jsonUnitPlurals);

        val.put("sign", indicator.getValue().getSign());
        val.put("hide", indicator.getValue().isHidden());

        obj.put("value", val);

        JSONObject frac = new JSONObject();
        frac.put("measure_unique_name", indicator.getDenominator().getMeasureUniqueName());

        JSONArray jsonFracUnits = new JSONArray();
        jsonFracUnits.addAll(indicator.getDenominator().getUnits());
        frac.put("units", jsonFracUnits);

        JSONArray jsonFracUnitPlurals = new JSONArray();
        jsonFracUnitPlurals.addAll(indicator.getDenominator().getUnitPlurals());
        frac.put("unitPlurals", jsonFracUnitPlurals);

        frac.put("sign", indicator.getDenominator().getSign());
        frac.put("hide", indicator.getDenominator().isHidden());
        frac.put("multiplier", indicator.getMultiplier().toString());

        obj.put("fraction", frac);

        return obj;
    }

    private static String base64encoder(final String origin) {
        final byte[] encodedBytes = Base64.getEncoder().encode(origin.getBytes());
        return new String(encodedBytes);
    }

}
