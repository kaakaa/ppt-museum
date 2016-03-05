# encoding: utf-8
# language: ja

機能: ppt-museumのアップロードテスト
  ppt-museumでは.ppt/.pptx/.pdfのアップロードをサポートしています。
  すべての拡張子のファイルがアップロードできることをテストします。

  背景: ppt-museumのアップロードページへ移動する
    前提 dbコンテナを初期化する
    もし ppt-museumにアクセスする
    ならば page_topが表示されている
    もし アップロードページへ移動する
    ならば page_uploadが表示されている

  シナリオ: ppt-museumにpptファイルをアップロードする
    前提 titleに「pptアップロードテスト」と入力する
    前提 descに「pptファイルのアップロードテストです」と入力する
    もし test.pptをアップロードする
    ならば pptアップロードテストがアップロードされている

  シナリオ: ppt-museumにpptxファイルをアップロードする
    前提 titleに「pptxアップロードテスト」と入力する
    前提 descに「pptxファイルのアップロードテストです」と入力する
    もし test.pptxをアップロードする
    ならば pptxアップロードテストがアップロードされている

  シナリオ: ppt-museumにpdfファイルをアップロードする
    前提 titleに「PDFアップロードテスト」と入力する
    前提 descに「pdfファイルのアップロードテストです」と入力する
    もし test.pdfをアップロードする
    ならば PDFアップロードテストがアップロードされている

