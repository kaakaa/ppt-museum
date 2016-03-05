# encoding: utf-8
require 'docker'

RESOURCES_DIR = File.expand_path("../../resources/", __FILE__)

step %(:containerコンテナを初期化する) do |service|
  Docker::Container.all
    .select{ |c| c.info['Labels']['com.docker.compose.service'] == service }
    .each { |c| c.exec(['/usr/bin/mongo', 'pptmuseum', '--eval', 'db.dropDatabase();'], detach: true) }
end

step "ppt-museumにアクセスする" do
  Capybara.app_host = "http://localhost:4567"
end

step "アップロードページへ移動する" do
  visit '/ppt-museum/upload'
end

step %(:fieldに「:value」と入力する) do |field, value|
  fill_in "#{field}", with: "#{value}"
end

step %(:path.:extをアップロードする) do |path, ext|
  attach_file "file", File.expand_path("#{path}.#{ext}", RESOURCES_DIR) 
  click_on "Upload"
end

step %(:textが表示されている) do |text|
  page.save_screenshot( File.expand_path("./snapshot/snap-#{text}.png"), full:true )
end

step %(:fileがアップロードされている) do |file|
  expect(page).to have_content(file)
  page.save_screenshot(
    File.expand_path("./snapshot/snap-#{file}.png"), full:true
  )
end
