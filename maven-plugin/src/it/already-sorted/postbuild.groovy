log = new File(basedir, 'build.log')
sorted = new File(basedir, 'pom.xml')
backup = new File(basedir, 'pom.xml.bak')

assert log.exists()
assert log.text.contains('Sorting file ' + sorted.absolutePath)
assert log.text.contains('Pom file is already sorted, exiting')
assert !backup.exists()

return true