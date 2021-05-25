


$(function() {
if( $('#lendingStatus').text() == '貸出し中' ){
//貸出し中の場合
  //ボタンの機能変更
    // 借りるボタンの非活性化
    $('.btn_rentBook').prop('disabled', true);
    // 返すボタンの活性化
    $('.btn_returnBook').prop('disabled', false);
    // 削除ボタンの非活性化
    $('.btn_deleteBook').prop('disabled', true);

  //ボタンの色変更
    //借りるボタンを薄い色に
    $('.btn_rentBook').toggleClass('btn_rentBookNG');
    // 返すボタンを濃い色に
    $('.btn_returnBookNG').toggleClass('btn_returnBook');
    // 削除ボタンを薄い色に
    $('.btn_deleteBook').toggleClass('btn_deleteBookNG');

  //カーソル機能
    //返すボタンのカーソル機能
    $('.btn_returnBook').hover(function() {
    $('.btn_returnBook').addClass('cursor_pointer');   
 }, function() {
    $('.btn_returnBook').removeClass('cursor_pointer');
});
    //返すボタンのオパシティ機能
    $('.btn_returnBook').hover(function() {
    $('.btn_returnBook').addClass('cursor_opacity');   
 }, function() {
    $('.btn_returnBook').removeClass('cursor_opacity');
}); 
}else{
//貸出し可の場合
  //ボタンの機能変更
    //借りるボタンの活性化
    $('.btn_rentBook').prop('disabled', false);
    //返すボタンの非活性化
    $('.btn_returnBook').prop('disabled', true);
    //削除ボタンの活性化
    $('.btn_deleteBook').prop('disabled', false);

  //ボタンの色変更
    //借りるボタンを濃い色に
    $('.btn_rentBookNG').toggleClass('btn_rentBook');
    // 返すボタンを薄い色に
    $('.btn_returnBook').toggleClass('btn_returnBookNG');
    // 削除ボタンを濃い色に
    $('.btn_deleteBookNG').toggleClass('btn_deleteBook');

  //カーソル機能
    //借りるボタンのカーソル機能
    $('.btn_rentBook').hover(function() {
    $('.btn_rentBook').addClass('cursor_pointer');
 }, function() {
    $('.btn_rentBook').removeClass('cursor_pointer');
});
    //削除ボタンのカーソル機能
    $('.btn_deleteBook').hover(function() {
    $('.btn_deleteBook').addClass('cursor_pointer');   
 }, function() {
    $('.btn_deleteBook').removeClass('cursor_pointer');
});
    //借りるボタンのオパシティ機能
    $('.btn_rentBook').hover(function() {
    $('.btn_rentBook').addClass('cursor_opacity');
 }, function() {
    $('.btn_rentBook').removeClass('cursor_opacity');
});
    //削除ボタンのオパシティ機能
    $('.btn_deleteBook').hover(function() {
    $('.btn_deleteBook').addClass('cursor_opacity');   
 }, function() {
    $('.btn_deleteBook').removeClass('cursor_opacity');
});
};
});