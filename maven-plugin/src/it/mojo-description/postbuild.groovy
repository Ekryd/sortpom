log = new File(basedir, 'build.log')
expected_log = new File(basedir, 'expected.log')

assert log.exists()
assert log.text.contains(expected_log.text.replaceAll('@pom.version@', projectversion))

return true