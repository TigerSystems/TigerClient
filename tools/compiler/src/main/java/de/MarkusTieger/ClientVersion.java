package de.MarkusTieger;

import java.util.Map;

public record ClientVersion(String client, Map<String, String> loader_versions, String loader_common) {

}
