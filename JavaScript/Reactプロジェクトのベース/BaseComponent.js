import React from "react"
import isEquivalent from "@ryo-kagawa/is-equivalent.js"

export default class BaseComponent extends React.Component {
  static isEquivalentProps = undefined;
  static isEquivalentState = undefined;
  static isEquivalentPropsOption = undefined;
  static isEquivalentStateOption = undefined;

  shouldComponentUpdate(nextProps, nextState) {
    return !isEquivalent(
      this.props,
      nextProps,
      this.constructor.isEquivalentProps,
      this.constructor.isEquivalentPropsOption
    ) || !isEquivalent(
      this.state,
      nextState,
      this.constructor.isEquivalentState,
      this.constructor.isEquivalentStateOption
    )
  }
}
