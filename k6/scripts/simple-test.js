import http from 'k6/http';
import { check, sleep } from 'k6';

// 간단한 부하 테스트 설정
export const options = {
  vus: 10, // 10명의 가상 사용자
  duration: '30s', // 30초 동안 실행
};

const BASE_URL = 'http://host.docker.internal:8080';

export default function () {
  // 헬스체크 엔드포인트 테스트
  let response = http.get(`${BASE_URL}/actuator/health`);

  check(response, {
    'status is 200': (r) => r.status === 200,
    'response time < 500ms': (r) => r.timings.duration < 500,
  });

  sleep(1);
}