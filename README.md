# DayGram

Native android diary application made with Android studio and Kotlin

[Google Slide](https://docs.google.com/presentation/d/1JV8ZpxUkhAn4ATgANTKQ7OQGjDD8qCmuUl9mMc4-3HI/edit?usp=sharing)


주요 기능
    글 작성
        카메라 버튼을 눌러 사진을 찍으면 글 작성 페이지로 이동한다
        제목과 내용을 입력 후 저장하면 SQLite DB에 추가한다
        메인 화면에서 DB와 RecyclerView를 동기화한다
    글 삭제
        메인 화면에서 카드 이미지를 오랫동안 누르고 있으면 삭제된다
    검색
        메인 화면 위의 검색 바에서 저장된 글 중 검색을 할 수 있다
