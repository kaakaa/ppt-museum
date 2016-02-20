module.exports = function(grunt) {
  grunt.initConfig({
    browserify: {
      dist: {
        files: {
          'src/main/resources/public/ppt-museum/js/pdf.js-controller.js': ['src/main/web/index.js']
        }
      }
    },
    cssmin : {
      dist: {
        src: ['node_modules/pdf.js-controller/css/pdf-slide.css'],
        dest: 'src/main/resources/public/ppt-museum/css/pdf-slide.min.css'
      }
    }
  });

  grunt.loadNpmTasks("grunt-browserify");
  grunt.loadNpmTasks("grunt-contrib-cssmin");

  grunt.registerTask("default", "build");
  grunt.registerTask("build", ["browserify", "cssmin"])
};
