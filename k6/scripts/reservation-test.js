import http from 'k6/http';
import { check } from 'k6';

export let options = {
  vus: 50,
  duration: '10s',
};

const BASE_URL = 'http://localhost:8080';
const API_ENDPOINT = '/api/v1/reservation';

export default function () {
  const res = http.post(BASE_URL + API_ENDPOINT, JSON.stringify({
    userId: `user_${__VU}`,
    concertId: 123,
    concertScheduleId: 456,
    seatId: 1
  }), { headers: { 'Content-Type': 'application/json' } });

  check(res, { '예약 성공': (r) => r.status === 200 });
}
