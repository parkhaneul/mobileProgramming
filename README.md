# DayGram

Diary application made with Android studio, Kotlin

[Google Slide](https://docs.google.com/presentation/d/1JV8ZpxUkhAn4ATgANTKQ7OQGjDD8qCmuUl9mMc4-3HI/edit?usp=sharing)

### UI 구성

메인 : Diary

- 로그인

    회원(다 만들고 시간 남으면 하자. 굳이 구현 안해도 됨)

    - 작성글 자동 동기화
    - 비회원
        - 로컬 SQL에 저장. 삭제시 초기화
        - 비회원 상태에서 회원으로 전환하면 한번에 동기화됨
- 순서(날짜)에 따라서 표기 (table view)
    - 목록에 있을 때 표기될 내용 : 날짜, 제목, 어디서, 누구와
    - 표기될 내용으로 필터링 가능할 것.
    - 삭제 및 변경도 필요.
- 제일 밑 혹은 제일 위에 새로운 글 작성
    - 작성시 필요한 내용
        - 제목(필수 / 없이 저장하면 내용 제일 첫 문장)
        - 내용(필수 / 없이 저장하면 제목과 동일)
        - 사진(선택 maximum 4? ← 전역변수로 빼서 수정할 수 있게 만들자.)
        - 날짜(작성시기로 자동 저장 변경 가능)
        - GPS(선택 / 어디서 ← 지도로 보여준다? 아니면 위치정보(주소)만 보여준다?)
        - 일단 GPS랑 사진은 위치정보 접근 권한이랑 앨범 및 카메라 접근 권한 허용 허락 받아야함.(AndroidStudio에서 app setting 가능할 거임)
    - 작성하기.
        - 저장(선택) - 로그인하면 서버에 저장, 안하면 로컬에 저장
    - 삭제하기
        - 예
        - 아니오
    - 변경하기
        - 접근 후 수정 시 나갈때 저장할지 말지 표시
            - 예
            - 아니요
