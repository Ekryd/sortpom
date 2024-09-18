log = new File(basedir, 'build.log')

assert log.exists()
assert log.text.contains('[ERROR] The line 8 is not considered sorted, should be \'    <relativePath></relativePath>\'\n')


return true
