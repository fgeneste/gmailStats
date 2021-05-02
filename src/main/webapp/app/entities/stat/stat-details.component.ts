import { Component, Vue, Inject } from 'vue-property-decorator';

import { IStat } from '@/shared/model/stat.model';
import StatService from './stat.service';

@Component
export default class StatDetails extends Vue {
  @Inject('statService') private statService: () => StatService;
  public stat: IStat = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.statId) {
        vm.retrieveStat(to.params.statId);
      }
    });
  }

  public retrieveStat(statId) {
    this.statService()
      .find(statId)
      .then(res => {
        this.stat = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
