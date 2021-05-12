import { Component, Vue, Inject } from 'vue-property-decorator';

import { IConf } from '@/shared/model/conf.model';
import ConfService from './conf.service';

@Component
export default class ConfDetails extends Vue {
  @Inject('confService') private confService: () => ConfService;
  public conf: IConf = {};

  beforeRouteEnter(to, from, next) {
    next(vm => {
      if (to.params.confId) {
        vm.retrieveConf(to.params.confId);
      }
    });
  }

  public retrieveConf(confId) {
    this.confService()
      .find(confId)
      .then(res => {
        this.conf = res;
      });
  }

  public previousState() {
    this.$router.go(-1);
  }
}
