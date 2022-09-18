package de.MarkusTieger.obf;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ObfuscationConfig {

	public static final String
				FORCE_PROCESSING = "-forceprocessing",			
				DONT_WARN = "-dontwarn",
				DONT_SHRINK = "-dontshrink",
				DONT_OPTIMIZE = "-dontoptimize",
				OVERLOAD_AGGRESIVLY = "-overloadaggressively",
				KEEP_ALL_ATTRIBUTES = "-keepattributes Exceptions,InnerClasses,Signature,Deprecated,SourceFile,LineNumberTable,LocalVariable*Table,*Annotation*,Synthetic,EnclosingMethod",
				DONT_NOTE = "-dontnote",
				IGNORE_WARNINGS = "-ignorewarnings";
	
	private final List<String> customLines = new ArrayList<>();
	private final Map<File, File> jars = new HashMap<>();
	private final List<File> libs = new ArrayList<>();
	
	
	private File mappingLoad, mappingSave;


	public String build() {
		List<String> lines = new ArrayList<>();
		
		for(Map.Entry<File, File> e : jars.entrySet()) {
			lines.add("-injars \'" + e.getKey().getAbsolutePath() + '\'');
			lines.add("-outjars \'" + e.getValue().getAbsolutePath() + '\'');
			lines.add("");
		}
		lines.add("");
		
		for(File lib : libs) {
			lines.add("-libraryjars \'" + lib.getAbsolutePath() + '\'');
			lines.add("");
		}
		
		lines.add("");
		
		lines.add("-printmapping \'" + mappingSave.getAbsolutePath() + '\'');
		lines.add("-applymapping \'" + mappingLoad.getAbsolutePath() + '\'');
		
		lines.add("");
		lines.add("");
		
		customLines.forEach((line) -> {
			lines.add(line);
			lines.add("");
		});
		
		String result = "";
		
		for(String line : lines) result += line + "\n";
		
		return result;
	}
	
}
