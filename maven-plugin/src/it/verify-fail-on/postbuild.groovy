log = new File(basedir, 'build.log')
sorted = new File(basedir, 'pom.xml')
violationFile = new File(basedir, 'target/sortpom_reports/violation.xml')
expected_violationFile = new File(basedir, 'expected_violation_file.xml')

assert log.exists()
assert log.text.contains('[INFO] Verifying file ' + sorted.absolutePath)
assert log.text.contains('[ERROR] The line 13 is not considered sorted, should be \'  </properties>\'')
assert log.text.contains('[INFO] Saving violation report to ' + violationFile.absolutePath)
assert log.text.contains('[ERROR] The file ' + sorted.absolutePath + ' is not sorted')
assert log.text.contains('[INFO] BUILD FAILURE')

assert expected_violationFile.text.replaceAll('@POM_PATH@', sorted.absolutePath).tokenize('\n').equals(violationFile.text.replaceAll('\r','').replaceAll('\\\\','').tokenize('\n'))

return true
